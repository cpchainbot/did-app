(ns cpc-did.pages.start-page.import-id
  (:require ["react-native" :as rn]
            ["ethers" :refer (ethers)]
            ["react-native-keyboard-aware-scroll-view" :refer (KeyboardAwareScrollView)]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [cpc-did.utils.react-navigation :refer [nav-reset]]
            [cpc-did.styles.import-id :as i-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.form-item :as form-item]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.utils.validates :as validate]
            [cpc-did.components.animations :as ani]
            [cpc-did.components.bottom-modal :as b-modal]
            [cpc-did.utils.http :as http]
            [cpc-did.utils.error :as err]
            [cpc-did.utils.event-listener :as listener :refer [add emit remove-listener]])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))
(defn page
  "docstring"
  [navigation state]
  (let [modal-visible (r/atom false)
        set-modal-visible #(swap! modal-visible not)
        keystore (r/atom nil)
        value (r/atom {})
        get-val (fn [key val]
                  (swap! value assoc key  val))
        fail #(emit listener/SHOW_ERROR (:message %))
        success (fn [data]
                  (emit listener/CLOSE nil)
                  (js/setTimeout
                    (fn []
                      (set-modal-visible)
                      (nav-reset navigation "import-username")) 500))]
    (r/create-class
      {:component-did-mount    (fn [this]
                                 (add listener/HTTP_ERROR fail)

                                 (add listener/HTTP_SUCCESS success))
       :reagent-render         (fn []
                                 [:> rn/View {:style layout/cross-axis-container}
                                  [:> KeyboardAwareScrollView
                                   (merge layout/keyboard-scroll-options
                                          layout/extra-height)
                                   [:> rn/View {:style i-style/hint-container}
                                    [:> rn/Text {:style i-style/hint} "请填入keystore"]]
                                   [:> rn/View {:style layout/keystore-container}
                                    [:> rn/TextInput {:style             i-style/keystore
                                                      :multiline         true
                                                      :blur-on-submit    true
                                                      :placeholder       "keystore"
                                                      :placeholder-color layout/placeholder-color
                                                      :auto-correct      false
                                                      :on-change-text    (fn [text]
                                                                           (reset! keystore text))}]]
                                   [form-item/normal-item
                                    "密码"
                                    "至少一位大小写英文字母和数字组合"
                                    validate/password?
                                    get-val
                                    i-style/form-item
                                    "password"]
                                   [:> rn/View {:style i-style/button-container}
                                    [m-button/normal-page-button
                                     (if (and
                                           (:password @value)
                                           (not= @keystore nil))
                                       true
                                       false)
                                     "导入"
                                     (fn []
                                       (set-modal-visible)
                                       ;(.show ^js @toast-ref (r/as-element [ani/toast-loading]) (.-FOREVER ^js DURATION))
                                       (emit listener/LOADING (r/as-element [ani/toast-loading]))
                                       (.runAfterInteractions ^js rn/InteractionManager
                                                              (fn []
                                                                (promise-> (fn [error]
                                                                             (emit listener/CLOSE nil)
                                                                             (set-modal-visible)
                                                                             (err/error {:code 601 :err error}))
                                                                           (.. ^js ethers
                                                                               -Wallet
                                                                               (fromEncryptedJson @keystore (:password @value)))
                                                                           (fn [wallet]
                                                                             (dispatch [:add-keystore (.stringify js/JSON @keystore)])
                                                                             (dispatch [:add-cpc-id (str "did:cpc:" (subs (.-address ^js wallet) 2))])
                                                                             (http/request-wrapper http/REGISTER_DID
                                                                                                   {:wallet wallet
                                                                                                    :did    (str "did:cpc:" (subs (.-address ^js wallet) 2))}))))))]]]

                                  [:> rn/Modal {:animationType "fade"
                                                :transparent   true
                                                :visible       @modal-visible}]])
       :component-will-unmount (fn [this]
                                 (remove-listener listener/HTTP_ERROR fail)
                                 (remove-listener listener/HTTP_SUCCESS success))})))
