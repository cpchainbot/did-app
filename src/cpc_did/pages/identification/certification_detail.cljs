(ns cpc-did.pages.identification.certification-detail
  (:require ["react-native" :as rn :refer [BackHandler]]
            [reagent.core :as r]
            [cpc-did.utils.date :as d]
            [re-frame.core :refer [subscribe]]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.certification-detail :as c-style]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.utils.event-listener :refer [listeners]]
            [cpc-did.utils.react-navigation :refer [nav-reset]]))


(defn page
  "docstring"
  [^js navigation state]
  (let [data (:params state)
        on-back (fn []
                  (nav-reset navigation "tab-nav")
                  true)]
    (r/create-class
      {:component-did-mount    (fn [this]
                                 (if (= (:routeName state) "cert-detail")
                                   (.addEventListener ^js BackHandler
                                                      "hardwareBackPress"
                                                      on-back)))

       :reagent-render         (fn []
                                 [:> rn/View {:style layout/flex-1}
                                  [:> rn/ScrollView {:shows-vertical-scroll-indicator false
                                                     :style c-style/scroll-view}
                                   [:> rn/View {:style c-style/info-container}
                                    [:> rn/View {:style c-style/image-container}
                                     [:> rn/Image {:source assets/credential-background}]
                                     [:> rn/View {:style c-style/info-content-container}
                                      [:> rn/View {:style c-style/info-content}
                                       [list-item/person-info-item assets/user-name "姓名" (:username data) 0]
                                       [list-item/person-info-item assets/id-card "身份证号" (:idCard data) 16]
                                       [list-item/person-info-item assets/vin "车辆VIN" (:VIN data) 16]]]]]
                                   [:> rn/View {:style c-style/detail-container}
                                    [:> rn/View {:style c-style/detail}
                                     [:> rn/Text {:style layout/item-label} "签发者"]
                                     [:> rn/Image {:source assets/logo-GAC-horizontal
                                                   :style  c-style/logo-margin}]

                                     [:> rn/Text {:style (merge layout/item-label c-style/group-margin)} "状态："]
                                     [:> rn/View {:style c-style/state-container}
                                      [:> rn/Image {:source assets/passed}]
                                      [:> rn/Text
                                       {:style (merge layout/item-content c-style/text-image-margin)}
                                       (:state data)]]

                                     [:> rn/Text {:style (merge layout/item-label c-style/group-margin)} "创建时间"]
                                     [:> rn/Text {:style (merge layout/item-content c-style/item-margin)} (d/to-time-string (:create-time data))]

                                     [:> rn/Text {:style (merge layout/item-label c-style/group-margin)} "有效期至"]
                                     [:> rn/Text {:style (merge layout/item-content c-style/item-margin)} (d/to-time-string (:expire-time data))]]
                                    [:> rn/Text {:style c-style/footer} "ATTESTED BY CPCHAIN"]]]])
       :component-will-unmount (fn [this]
                                 (if (= (:routeName state) "cert-detail")
                                   (.removeEventListener ^js BackHandler "hardwareBackPress" on-back)))})))

