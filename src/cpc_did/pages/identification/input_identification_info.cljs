(ns cpc-did.pages.identification.input-identification-info
  (:require [reagent.core :as r]
            ["js-base64" :refer (Base64)]
            ["react-native" :as rn]
            ["react-native-keyboard-aware-scroll-view" :refer (KeyboardAwareScrollView)]
            [re-frame.core :refer [subscribe]]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.input-identification-info :as i-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.form-item :as form-item]
            [cpc-did.utils.validates :as validate]
            [cpc-did.components.animations :as ani]
            [cpc-did.utils.react-navigation :refer [nav-reset]]
            [cpc-did.components.bottom-modal :as b-modal]
            [cpc-did.utils.http :as http]
            [cpc-did.utils.event-listener :as listener :refer [add emit remove-listener]]))


;;; Switch between modal will have some side effects, two ways to solve it:
;;; 1. setTimeout
;;; 2. switch state in dismiss function.
(defn loading-modal
  "It can active some modals: failure, bottom, repeat"
  [visible]
  [:> rn/Modal {:animationType "fade"
                :transparent   false
                :visible       @visible}
   [:> rn/View {:style layout/container}
    [:> rn/View {:style i-style/ani-container}
     [ani/loading]]
    [:> rn/Text {:style i-style/loading-modal-title} "正在签发…"]
    [:> rn/Text {:style i-style/loading-modal-content} "时间很短，请不要离开此页面"]]])

(defn failure-modal
  "Just edit repeatedly, or return tab-nav"
  [visible navigation set-failure-visible failure-info]
  (let [back-func (fn []
                    (set-failure-visible)
                    (nav-reset navigation "tab-nav"))]
    (fn [visible navigation set-failure-visible failure-info]
      [:> rn/Modal {:animationType "fade"
                    :transparent   false
                    :visible       @visible
                    :onRequestClose back-func}
       [:> rn/View {:style layout/container}
        [:> rn/Image {:source assets/res-failure
                      :style  layout/res-image}]
        [:> rn/Text {:style layout/res-title-deep} "认证失败"]
        [:> rn/Text {:style layout/res-content-deep} @failure-info]

        [:> rn/View {:style layout/ident-first-button}
         [m-button/normal-page-button true "重新编辑 " #(set-failure-visible)]]
        [m-button/text-button "回首页" back-func 24]]])))

(defn repeat-modal
  "Just edit repeatedly, or return tab-nav"
  [visible navigation set-repeat-visible]
  (let [back-func (fn []
                    (set-repeat-visible)
                    (nav-reset navigation "tab-nav"))]
    (fn [visible navigation set-repeat-visible]
      [:> rn/Modal {:animationType "fade"
                    :transparent   false
                    :visible       @visible
                    :onRequestClose back-func}
       [:> rn/View {:style layout/container}
        [:> rn/Image {:source assets/res-warning
                      :style  layout/res-image}]
        [:> rn/Text {:style layout/res-title-deep} "重复认证"]
        [:> rn/Text {:style layout/res-content-deep} "该VIN认证记录已存在"]

        [:> rn/View {:style layout/ident-first-button}
         [m-button/normal-page-button true "重新编辑 " (fn []
                                                     (set-repeat-visible))]]
        [m-button/text-button "回首页" back-func 24]]])))

(defn page
  "The main page"
  [^js navigation state]
  (let [value (r/atom {})
        failure-info (r/atom "")
        claim-detail @(subscribe [:claim-detail])

        loading-visible (r/atom false)
        set-loading-visible #(swap! loading-visible not)
        failure-visible (r/atom false)
        set-failure-visible #(swap! failure-visible not)

        repeat-visible (r/atom false)
        set-repeat-visible #(swap! repeat-visible not)
        bottom-visible (r/atom false)
        set-bottom-visible #(swap! bottom-visible not)

        get-wallet-success #(http/request-wrapper http/GET_DATA_WITHOUT_TOKEN
                                                  {:event http/get-credential-wrapper
                                                   :wallet %
                                                   :claim (http/base64-encode (merge claim-detail
                                                                                     @value))})
        get-val (fn [key val]
                  (swap! value assoc key val))

        fail (fn [data]
               (reset! failure-info (:message data))
               (set-loading-visible)
               (case (:code data)
                 703 (do
                       (emit listener/SHOW_ERROR (:message data))
                       (ani/delay-job set-bottom-visible 500))
                 605 nil
                 (ani/after-animation set-failure-visible)))
        success (fn [data]
                  (set-loading-visible)
                  (nav-reset navigation "cert-detail" data))]
    (r/create-class
      {:component-did-mount (fn [this]
                              ;; http error include:
                              ;; 1. bad network, set loading visible false
                              ;; 2. invalid token, loading modal visible
                              ;; 3. other problem (repeat may need be independent)
                              (add listener/HTTP_ERROR fail)
                              (add listener/HTTP_SUCCESS success))
       :reagent-render (fn []
                         [:> rn/View {:style layout/cross-axis-container}
                          [:> KeyboardAwareScrollView
                           (merge layout/keyboard-scroll-options i-style/extra-height)
                           [:> rn/View {:style i-style/intro-container}
                            [:> rn/Image {:source assets/safe-icon}]
                            [:> rn/Text {:style i-style/hint-info}
                             "广汽集团将认证你填写的个人身份信息，没有你的授权，任何人都无法获取你的隐私信息。"]]
                           [form-item/normal-item
                            ; (:username claim)
                            "真实姓名"
                            "姓名"
                            validate/person-name?
                            get-val
                            i-style/first-item
                            "name"]
                           [form-item/normal-item
                            ;(:idCard claim)
                            "身份证"
                            "身份证号码"
                            validate/id-card?
                            get-val
                            i-style/other-item
                            "postalCode"]
                           [form-item/vin-item
                            ; (:VIN claim)
                            "VIN"
                            "VIN编码"
                            validate/vin?
                            get-val
                            i-style/other-item
                            navigation]
                           [m-button/hint-button 16 "什么是VIN？" #(.navigate navigation "vin-help")]
                           [:> rn/View {:style i-style/button-container}
                            [m-button/normal-page-button
                             (if (and
                                   (:username @value)
                                   (:idCard @value)
                                   (:VIN @value))
                               true
                               false)
                             "提交认证"
                             (fn []
                               (set-loading-visible)
                               (http/request-wrapper http/GET_CREDENTIAL {:claim (http/base64-encode (merge claim-detail
                                                                                                            @value))
                                                                          :token @(subscribe [:token])}))]]]
                          [loading-modal loading-visible]
                          [repeat-modal repeat-visible navigation repeat-visible]
                          [failure-modal failure-visible navigation set-failure-visible failure-info]
                          [b-modal/bottom-http bottom-visible set-bottom-visible set-loading-visible get-wallet-success]])
       :component-will-unmount (fn [this]
                                 (remove-listener listener/HTTP_ERROR fail)
                                 (remove-listener listener/HTTP_SUCCESS success))})))
