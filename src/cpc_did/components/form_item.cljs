(ns cpc-did.components.form-item
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.m-icon :as m-icon]
            [cpc-did.styles.components.form-item :as f-style]))

;;; -- helper function -----------------------------------------------------
(def key-list
  {"密码"   :password
   "用户昵称" :username
   "确认密码" :ack-password
   "真实姓名" :username
   "身份证"  :idCard
   "VIN"  :VIN})

(defn debounce
  "Delay a piece of time to validate input
  This is useful to prevent sifting cursor"
  [func]
  (let [timer (atom nil)]
    (fn [text]
      (js/clearTimeout @timer)
      (reset! timer (js/setTimeout
                      #(func text)
                      800)))))

;;; -- component --------------------------------------------------------
(defn text-input
  "docstring"
  [label placeholder validate get-value type]
  (let [ref (atom nil)
        v-res (atom nil)
        icon (r/atom nil)
        border-color (r/atom f-style/blur-border)]
    (fn [label placeholder validate get-value type]
      [:> rn/View {:style (merge f-style/input-box-container
                                 @border-color)}
       [:> rn/View {:style f-style/input-box}
        [:> rn/TextInput {:max-length        24
                          :ref               #(reset! ref %)
                          :on-blur           #(if (not= @v-res false) (reset! border-color f-style/blur-border))
                          :on-focus          #(if (not= @v-res false) (reset! border-color f-style/focus-border))
                          :style             f-style/input
                          :placeholder       placeholder
                          :text-content-type type
                          :secure-text-entry (if (= type "password") true false)
                          :on-change-text    (debounce
                                               (fn [text]
                                                 (reset! v-res (validate text))
                                                 (if @v-res
                                                   (do
                                                     (get-value (key-list label) text)
                                                     (reset! icon assets/input-passed)
                                                     (if (.isFocused ^js @ref)
                                                       (reset! border-color f-style/focus-border)
                                                       (reset! border-color f-style/blur-border)))
                                                   (do
                                                     (get-value (key-list label) nil)
                                                     (reset! border-color f-style/err-border)
                                                     (reset! icon assets/input-fail)))))}]
        (if @icon
          [m-icon/normal-icon @icon (if (not @v-res)
                                      (fn []
                                        (.clear ^js @ref)
                                        (.focus ^js @ref)
                                        (reset! icon nil)
                                        (reset! border-color f-style/focus-border))
                                      nil)])]])))


(defn normal-item
  [label placeholder validate get-value style type]
  [:> rn/View {:style (merge f-style/container
                             style)}
   [:> rn/Text {:style f-style/label} label]
   [text-input label placeholder validate get-value type]])


(defn vin-input
  "docstring"
  [label placeholder validate get-value default-value icon v-res]
  (let [ref (atom nil)
        border-color (r/atom f-style/blur-border)]
    (fn [label placeholder validate get-value default-value icon v-res]
      [:> rn/View {:style (merge f-style/input-box-container
                                 @border-color)}
       [:> rn/View {:style f-style/input-box}
        [:> rn/TextInput {:max-length        24
                          :ref               #(reset! ref %)
                          :on-blur           #(if (not= @v-res false) (reset! border-color f-style/blur-border))
                          :on-focus          #(if (not= @v-res false) (reset! border-color f-style/focus-border))
                          :style             f-style/input
                          :placeholder       placeholder
                          :default-value     @default-value
                          :text-content-type "postalCode"
                          :on-change-text    (debounce
                                               (fn [text]
                                                 (reset! v-res (validate text))
                                                 (if @v-res
                                                   (do
                                                     (get-value (key-list label) text)
                                                     (reset! icon assets/input-passed)
                                                     (if (.isFocused ^js @ref)
                                                       (reset! border-color f-style/focus-border)
                                                       (reset! border-color f-style/blur-border)))
                                                   (do
                                                     (get-value (key-list label) nil)
                                                     (reset! border-color f-style/err-border)
                                                     (reset! icon assets/input-fail)))))}]
        (if @icon
          [m-icon/normal-icon @icon (if (not @v-res)
                                      (fn []
                                        (.clear ^js @ref)
                                        (.focus ^js @ref)
                                        (reset! icon nil)
                                        (reset! border-color f-style/focus-border))
                                      nil)])]])))

(defn vin-item
  [label placeholder validate get-value style ^js navigation]
  (let [vin (r/atom nil)
        icon (r/atom nil)
        v-res (atom nil)
        success (fn [data]
                  (get-value (key-list label) data)
                  (reset! vin data)
                  (reset! v-res true)
                  (reset! icon assets/input-passed))]
    (fn [label placeholder validate get-value style ^js navigation]
      [:> rn/View {:style (merge f-style/container style)}
       [:> rn/View {:style f-style/label-container}
        [:> rn/Text {:style f-style/label} label]
        [m-icon/normal-icon assets/qr-scan #(.navigate navigation "vin-scanner" success)]]
       [vin-input label placeholder validate get-value vin icon v-res]])))
