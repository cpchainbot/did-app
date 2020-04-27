(ns cpc-did.pages.qr-code.authorize
  (:require ["react-native" :as rn]
            ["expo-linear-gradient" :as lg]
            ["react-native-shadow" :refer (BorderShadow BoxShadow)]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [cpc-did.db :refer [get-vc]]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.authorize :as a-style]
            [cpc-did.styles.auth-result :as auth-res]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.utils.cljs-js-transit :as t]
            [cpc-did.components.animations :as ani]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.utils.react-navigation :refer [nav-reset]]
            [cpc-did.utils.event-listener :as listener :refer [add emit remove-listener]]
            [cpc-did.components.bottom-modal :as b-modal]
            [cpc-did.utils.http :as http]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.utils.date :refer [to-time-string]])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn m-item
  [title expire-time index this-item tag set-tag set-value]
  [:> BoxShadow {:setting a-style/custom-options}
   [:> rn/View {:style a-style/i-container}
    [:> rn/View {:style a-style/i-content}
     [:> rn/View {:style a-style/info-container}
      [list-item/two-item-left assets/list-car title layout/list-title]
      [:> rn/View {:style a-style/list-detail-container}
       [:> rn/Text {:style layout/label} "有效期至："]
       [:> rn/Text {:style layout/content} (to-time-string expire-time)]]]
     [:> rn/TouchableOpacity {:on-press (fn []
                                          (set-tag index)
                                          (set-value this-item))}
      [:> rn/Image {:source (if (= @tag index)
                              assets/circle-checked
                              assets/circle-unchecked)}]]]]])

(defn verify-box
  "docstring"
  [is-touchable on-press]
  [:> rn/View {:style a-style/verify-box-container}
   [:> BorderShadow
    {:setting a-style/shadow-options}
    [:> rn/View {:style a-style/verify-box
                 :key   "child_1"}
     [m-button/normal-page-button
      is-touchable
      "验证"
      on-press]]
    [:> rn/View {:key "child_2"}]]])

(defn loading-modal
  "It can active some modals: failure, bottom"
  [visible]
  [:> rn/Modal {:animationType "fade"
                :transparent   false
                :visible       @visible}
   [:> rn/View {:style layout/container}
    [:> rn/View {:style a-style/ani-container}
     [ani/loading]]
    [:> rn/Text {:style a-style/loading-modal-title} "正在验证…"]
    [:> rn/Text {:style a-style/loading-modal-content} "时间很短，请不要离开此页面"]]])

(defn failure-modal
  "Active a modal in dismiss
  transducer: nil"
  [visible ^js navigation set-failure-visible failure-info]
  (let [back-func (fn []
                    (set-failure-visible)
                    (nav-reset navigation "tab-nav"))]
    (fn [visible ^js navigation set-failure-visible failure-info]
      [:> rn/Modal {:animationType  "fade"
                    :transparent    false
                    :visible        @visible
                    :onRequestClose back-func}
       [:> lg/LinearGradient {:style  layout/container
                              :colors color/linear-colors
                              :start  #js [0 0]
                              :end    #js[0 1]}
        [:> rn/Image {:source assets/res-failure
                      :style  (merge layout/res-image
                                     auth-res/res-image)}]
        [:> rn/Text {:style layout/res-title-shallow} "验证失败"]
        [:> rn/Text {:style layout/res-content-shallow} @failure-info]

        [:> rn/View {:style layout/auth-first-button}
         [m-button/start-page-button true "重新扫码" (fn []
                                                   (set-failure-visible)
                                                   (.goBack navigation))]]
        [:> rn/View {:style auth-res/button-gap}
         [m-button/start-page-button false "回首页" back-func]]]])))

(defn alert-modal
  "show qr-code information"
  [visible set-visible qr-data ^js navigation]
  [:> rn/Modal {:visible       @visible
                :transparent   true
                :animationType "fade"}
   [:> rn/View {:style layout/transparent-modal-background}
    [:> rn/View {:style a-style/alert-modal-container}
     [:> rn/TouchableOpacity {:style    a-style/close-icon-container
                              :on-press (fn []
                                          (set-visible)
                                          (.goBack navigation))}
      [:> rn/Image {:source assets/close}]]
     [:> rn/View {:style a-style/alert-info-container}
      [:> rn/Text {:style a-style/item-label} "验证方"]
      [:> rn/Text {:style a-style/item-content} (:supplier qr-data)]
      [:> rn/Text {:style a-style/item-label} "地点"]
      [:> rn/Text {:style a-style/item-content} (:location qr-data)]
      [:> rn/Text {:style a-style/alert-hint-info} "您的认证信息将以加密的形式发送给对方，请放心使用。"]]
     [m-button/small-page-button true "确定" set-visible]]]])

(defn page
  "docstring"
  [navigation state]
  (let [qr-data (:params state)
        tag (r/atom 0)
        set-tag (fn [index]
                  (reset! tag index))
        value (r/atom (@(subscribe [:cert-list]) 0))
        set-value (fn [val]
                    (reset! value val))

        params (atom nil)

        alert-visible (r/atom false)
        set-alert-visible #(swap! alert-visible not)

        failure-visible (r/atom false)
        set-failure-visible #(swap! failure-visible not)

        loading-visible (r/atom false)
        set-loading-visible #(swap! loading-visible not)

        bottom-visible (r/atom false)
        set-bottom-visible #(swap! bottom-visible not)

        get-wallet-success (fn [wallet]
                             (promise-> #(println %)
                                        (get-vc @tag)
                                        (fn [vc]
                                          (reset! params {:vc          (:cred vc)
                                                          :create-time (:create-time @value)
                                                          :expire-time (:expire-time @value)
                                                          :location    (:location qr-data)
                                                          :wallet      wallet
                                                          :holder      @(subscribe [:cpc-id])
                                                          :verifier    (:verifier qr-data)
                                                          :supplier    (:supplier qr-data)
                                                          :type        (:type @value)
                                                          :token       @(subscribe [:token])})
                                          (if @(subscribe [:is-register-did])
                                            (http/request-wrapper http/GET_VERIFICATION @params)
                                            (http/request-wrapper http/GET_VERIFICATION_WITHOUT_REGISTER @params)))))

        verify-fn (fn []
                    (set-bottom-visible))
        failure-info (r/atom nil)

        fail (fn [data]
               (reset! failure-info (:message data))
               (case (:code data)
                 703 (do
                       (emit listener/SHOW_ERROR (:message data))
                       (http/request-wrapper http/GET_DATA_WITHOUT_TOKEN (merge @params
                                                                                {:event http/get-verification-wrapper})))
                 605 (set-loading-visible)
                 (do
                   (set-loading-visible)
                   (ani/after-animation set-failure-visible))))
        success (fn [data]
                  (set-loading-visible)
                  (nav-reset navigation "auth-success" data))]

    (r/create-class
      {:component-did-mount    (fn [this]
                                 ;; http error include:
                                 ;; 1. bad network, set loading visible false
                                 ;; 2. invalid token, use another request function
                                 ;; 3. other problem (repeat may need be independent)
                                 (add listener/HTTP_ERROR fail)
                                 (add listener/HTTP_SUCCESS success)
                                 (set-alert-visible))
       :reagent-render         (fn []
                                 [:> rn/View {:style layout/cross-axis-container}
                                  [:> rn/FlatList {:data                            @(subscribe [:cert-list])
                                                   :render-item                     (fn [^js item]
                                                                                      (let [{:keys [type expire-time] :as this-item} (-> item
                                                                                                                                         .-item
                                                                                                                                         t/->cljs)
                                                                                            index (.-index item)]
                                                                                        (r/as-element
                                                                                          [m-item type expire-time index this-item tag set-tag set-value])))
                                                   :key-extractor                   (fn [_ index]
                                                                                      (str index))
                                                   :List-footer-component           list-item/flat-list-footer
                                                   :shows-vertical-scroll-indicator false}]
                                  [:> rn/View {:style a-style/bottom-placeholder}]
                                  [verify-box qr-data verify-fn]
                                  [alert-modal alert-visible set-alert-visible qr-data navigation]
                                  [loading-modal loading-visible]
                                  [failure-modal failure-visible navigation set-failure-visible failure-info]
                                  [b-modal/bottom-http bottom-visible set-bottom-visible set-loading-visible get-wallet-success]])
       :component-will-unmount (fn [this]
                                 (remove-listener listener/HTTP_ERROR fail)
                                 (remove-listener listener/HTTP_SUCCESS success))})))
