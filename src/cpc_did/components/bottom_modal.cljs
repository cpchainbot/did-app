(ns cpc-did.components.bottom-modal
  (:require ["react-native" :as rn :refer (Keyboard)]
            ["ethers" :refer (ethers)]
            ["react-native-easy-toast" :default Toast :refer (DURATION)]
            [reagent.core :as r]
            [cpc-did.utils.error :as err]
            [re-frame.core :refer [subscribe]]
            [cpc-did.components.m-icon :as m-icon]
            [cpc-did.utils.validates :as validate]
            [cpc-did.components.animations :as ani]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.components.form-item :as f-style]
            [cpc-did.styles.components.bottom-modal :as b-style]
            [cpc-did.utils.event-listener :as listener :refer [emit]])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

;;; switch gap between two modal
(defn delay-modal-switch
  "docstring"
  [func]
  (js/setTimeout func 30))

(defn bottom-modal
  "A bottom modal used to input password"
  [set-modal-visible set-bottom-visible success-call]
  (let [password (r/atom "")
        ref (r/atom nil)
        border-color (r/atom f-style/blur-border)]
    (fn [set-modal-visible set-bottom-visible success-call]
      [:> rn/KeyboardAvoidingView {:behavior "height"
                                   :style b-style/modal-container}
       [:> rn/TouchableOpacity {:on-press (fn []
                                            (set-modal-visible)
                                            (set-bottom-visible)
                                            (reset! password "")
                                            (reset! border-color f-style/blur-border))
                                :style    b-style/touchable}
        [:> rn/View]]
       [:> rn/View {:style b-style/content-container}
        [:> rn/Text {:style b-style/modal-title} "账户密码"]
        [:> rn/View {:style (merge
                              b-style/input-container
                              @border-color)}
         [:> rn/TextInput {:max-length        24
                           :ref               #(reset! ref %)
                           :style             (merge f-style/input b-style/input)
                           :placeholder       "请输入密码"
                           :text-content-type "password"
                           :on-blur #(reset! border-color f-style/blur-border)
                           :on-focus #(reset! border-color f-style/focus-border)
                           :secure-text-entry true
                           :on-change-text    #(reset! password %)}]]
        [m-button/normal-page-button
         true
         "确认"
         (fn []
           (set-bottom-visible)
           (emit listener/LOADING (r/as-element [ani/toast-loading]))
           (.runAfterInteractions ^js rn/InteractionManager
                                  #(promise-> (fn [error]
                                                (set-modal-visible)
                                                (emit listener/CLOSE nil)
                                                (err/error {:code 601 :err error})
                                                (println error))
                                              (.. ^js ethers
                                                  -Wallet
                                                  (fromEncryptedJson (.parse js/JSON @(subscribe [:keystore])) @password))
                                              (fn [wallet]
                                                (emit listener/CLOSE nil)
                                                (set-modal-visible)
                                                (success-call wallet)))))]]])))

(defn bottom-http
  "A simple modal used to input password, when has http request"
  [visible set-bottom-visible set-loading-visible success-call]
  (let [password (r/atom "")
        ref (r/atom nil)
        border-color (r/atom f-style/blur-border)]
    (fn [visible set-bottom-visible set-loading-visible success-call]
      [:> rn/Modal {:animationType "fade"
                    :transparent   true
                    :visible @visible}
       [:> rn/KeyboardAvoidingView {:behavior "height"
                                    :style b-style/modal-container}
        ; To add it. touchable blank, and return.
        ;[:> rn/TouchableOpacity {:on-press (fn []
        ;                                     (set-modal-visible)
        ;                                     (set-bottom-visible)
        ;                                     (reset! password "")
        ;                                     (reset! is-touchable false)
        ;                                     (reset! is-secure true)
        ;                                     (reset! v-res "hide")
        ;                                     (reset! border-color f-style/blur-border))
        ;                         :style    b-style/touchable}
        ; [:> rn/View]]
        [:> rn/View {:style b-style/content-container}
         [:> rn/Text {:style b-style/modal-title} "账户密码"]
         [:> rn/View {:style (merge
                               b-style/input-container
                               @border-color)}
          [:> rn/TextInput {:max-length        24
                            :ref               #(reset! ref %)
                            :style             (merge f-style/input b-style/input)
                            :placeholder       "请输入密码"
                            :text-content-type "password"
                            :secure-text-entry true
                            :on-focus          #(reset! border-color f-style/focus-border)

                            :on-blur           #(reset! border-color f-style/blur-border)
                            :on-change-text    #(reset! password %)}]]
         [m-button/normal-page-button
          true
          "确认"
          (fn []
            (set-bottom-visible)
            (set-loading-visible)
            (ani/delay-job #(promise-> (fn [error]
                                         (set-loading-visible)
                                         (ani/delay-job set-bottom-visible 500)
                                         (err/error {:code 601 :err error})
                                         (println error))
                                       (.. ^js ethers
                                           -Wallet
                                           (fromEncryptedJson (.parse js/JSON @(subscribe [:keystore])) @password))
                                       (fn [wallet]
                                         (success-call wallet))) 1500))]]]])))
