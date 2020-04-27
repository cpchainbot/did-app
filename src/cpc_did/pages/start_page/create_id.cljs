(ns cpc-did.pages.start-page.create-id
  (:require ["react-native" :as rn]
            ["ethers" :refer (ethers)]
            ["react-native-keyboard-aware-scroll-view" :refer (KeyboardAwareScrollView)]
            ["lottie-react-native" :as LottieView]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [cpc-did.utils.react-navigation :refer [nav-reset]]
            [cpc-did.components.form-item :as form-item]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.styles.create-id :as c-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.utils.validates :as validate]
            [cpc-did.components.animations :as ani]
            [cpc-did.utils.error :as err]
            [cpc-did.utils.http :as http]
            [cpc-did.utils.event-listener :as listener :refer [add emit remove-listener]])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn create-wallet
  [password]
  (ani/delay-job (fn []
                   (let [random-wallet (.. ^js ethers
                                           -Wallet
                                           createRandom)]

                     (promise-> #(err/error {:code 602 :err %})
                                (.encrypt ^js random-wallet
                                          password
                                          #js {:scrypt #js {:N 2 :p 1 :r 8}})
                                (fn [keystore]
                                  (dispatch [:add-keystore (.stringify js/JSON keystore)])
                                  (dispatch [:add-cpc-id (str "did:cpc:" (subs (.-address random-wallet) 2))])
                                  (http/request-wrapper http/REGISTER_DID {:wallet random-wallet
                                                                           :did (str "did:cpc:" (subs (.-address random-wallet) 2))})))))
                 30))

(defn modal
  "docstring"
  [visible]
  [:> rn/Modal {:animationType "fade"
                :transparent   false
                :visible       @visible}
   [:> rn/View {:style layout/cross-axis-container}
    [:> rn/View {:style c-style/ani-container}
     [ani/loading]]
    [:> rn/View {:style c-style/intro-container}
     [:> rn/Text {:style c-style/intro-title} "什么是 CPC ID?"]
     [:> rn/Text {:style c-style/intro-detail} "CPC ID是区块链世界的通行证，基于去中心化身份标识协议，实现现实世界中的人、财、物、事在链上的标识、认证和通证。"]]]])

(defn page
  "docstring"
  [^js navigation state]
  (let [visible (r/atom false)
        set-visible #(swap! visible not)
        value (r/atom {})
        get-val (fn [key val]
                  (swap! value assoc key val))
        fail #(emit listener/SHOW_ERROR (:message %))
        success (fn [data]
                  (set-visible)
                  (nav-reset navigation "create-result"))]
    (r/create-class
      {:component-did-mount (fn [this]
                              (add listener/HTTP_ERROR fail)
                              (add listener/HTTP_SUCCESS success))
       :reagent-render (fn []
                         [:> rn/View {:style layout/cross-axis-container}
                          [:> KeyboardAwareScrollView
                           (merge layout/keyboard-scroll-options
                                  c-style/extra-height)
                           [form-item/normal-item
                            "用户昵称"
                            "支持汉字/数字/字母"
                            validate/username?
                            get-val
                            c-style/first-form-item
                            "username"]
                           [form-item/normal-item
                            "密码"
                            "至少一位大小写英文字母和数字组合"
                            validate/password?
                            get-val
                            c-style/other-form-item
                            "password"]
                           [form-item/normal-item
                            "确认密码"
                            "重复密码"
                            (fn [val]
                              (and val
                                   (validate/password? val)
                                   (= (:password @value) val)))
                            get-val
                            c-style/other-form-item
                            "password"]
                           [:> rn/View {:style c-style/button-container}
                            [m-button/normal-page-button
                             (if (and
                                   (:username @value)
                                   (:password @value)
                                   (:ack-password @value))
                               true
                               false)
                             "创建"
                             (fn []
                               (reset! visible true)
                               (dispatch [:add-username (:username @value)])
                               (create-wallet (:password @value)))]]]
                          [modal visible]])
       :component-will-unmount (fn [this]
                                 (remove-listener listener/HTTP_ERROR fail)
                                 (remove-listener listener/HTTP_SUCCESS success))})))
