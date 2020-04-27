(ns cpc-did.components.camera
  (:require ["react-native" :as rn :refer (StyleSheet Alert)]
            ["expo-permissions" :as permission]
            ["expo-camera" :refer (Camera)]
            ["react-navigation" :refer (withNavigationFocus)]
            [reagent.core :as r]
            [cpc-did.utils.cljs-js-transit :as t]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.components.camera :as c-style]
            [cpc-did.components.gradient-line :as g-line]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.m-icon :as icon])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn camera-animation
  [is-focused]
  (let [shift-y (r/atom (new (.-Value ^js rn/Animated) 0))
        start (fn m-animate []
                (-> ^js @shift-y
                    (.setValue 0))
                (-> ^js rn/Animated
                    (.timing @shift-y
                             #js {:toValue  1
                                  :duration 4000
                                  :easing   (.-linear ^js rn/Easing)})
                    (.start (fn [^js obj]
                              (if (.-finished obj)
                                (m-animate))))))
        stop #(.stopAnimation ^js @shift-y)
        y (.interpolate ^js @shift-y
                        (t/->js
                          {:inputRange  [0 0.5 1]
                           :outputRange c-style/ani-line-output}))]
    (if is-focused
      (do
        (start)
        [:> (.-View rn/Animated) {:style {:transform [{:translateY y}]}}
         [g-line/gradient-line-qr]])
      (do
        (stop)
        [:> rn/View]))))

(defn camera
  [title hint-info back-func scanned-func]
  [:> Camera {:useCamera2Api       true
              :ratio               "16:9"
              :style               (.-absoluteFillObject ^js StyleSheet)
              :on-bar-code-scanned scanned-func}
   [:> rn/View {:style c-style/layer-top}
    [:> rn/TouchableOpacity {:style    c-style/header-left
                             :on-press back-func}

     [:> rn/Image {:source assets/arrow-back}]
     [:> rn/Text {:style c-style/title} title]]]
   [:> rn/View {:style c-style/layer-center}
    [:> rn/View {:style c-style/center-left}]
    [:> rn/View {:style c-style/center-center}
     [:> (withNavigationFocus (r/reactify-component
                                (fn [{:keys [isFocused]}]
                                  [camera-animation isFocused])))]
     [:> rn/View {:style c-style/left-top}]
     [:> rn/View {:style c-style/left-bottom}]
     [:> rn/View {:style c-style/right-top}]
     [:> rn/View {:style c-style/right-bottom}]]
    [:> rn/View {:style c-style/center-right}]]
   [:> rn/View {:style c-style/layout-bottom}
    [:> rn/Text {:style c-style/hint-info} hint-info]]])


(defn scanner
  [title hint-info back-func scanned-func]
  (let [has-camera-permission (r/atom nil)]
    (r/create-class
      {:component-did-mount (fn [this]
                              (let [perm (t/->cljs permission)]
                                (promise-> #(println %)
                                           ((:askAsync perm) (:CAMERA perm))
                                           (fn [value]
                                             (let [status (:status (t/->cljs value))]
                                               (reset! has-camera-permission (identical?
                                                                               status
                                                                               "granted"))
                                               (if (= @has-camera-permission false)
                                                 (js/setTimeout #(.alert Alert
                                                                         "无相机权限"
                                                                         "点击确认按钮返回"
                                                                         (clj->js [{:text "确认" :onPress back-func}])) 50)))))))
       :reagent-render      (fn []
                              (let [perm @has-camera-permission]
                                (case perm
                                  nil [:> rn/View {:style c-style/container}]
                                  false [:> rn/View {:style (merge layout/container-center c-style/container)}
                                         [:> rn/Text "没有相机权限"]]
                                  [camera title hint-info back-func scanned-func])))})))
