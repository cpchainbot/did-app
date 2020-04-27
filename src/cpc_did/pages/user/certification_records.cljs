(ns cpc-did.pages.user.certification-records
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            ["react-native-shadow" :refer (BoxShadow)]
            [re-frame.core :refer [subscribe]]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.styles.certification-records :as c-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.utils.cljs-js-transit :as t]))

(defn m-item
  [^js navigation title VIN item]
  [:> BoxShadow {:setting c-style/custom-options}
   [:> rn/View {:style c-style/i-container}
    [:> rn/TouchableOpacity {:style c-style/i-content
                             :on-press #(.navigate navigation "cert-record-detail" item)}
     [list-item/three-item assets/list-car title layout/list-title assets/arrow-enter]
     [:> rn/View {:style c-style/info-container}
      [:> rn/Text {:style layout/label} "VIN："]
      [:> rn/Text {:style layout/content} VIN]]]]])

(defn page
  "docstring"
  [navigation state]
  (r/as-element
    [:> rn/View {:style layout/cross-axis-container}
     ;[:> rn/View {:style c-style/count-container}
     ; [:> rn/Text {:style c-style/count-text} (str "共" @(subscribe [:cert-count]) "条认证")]
     ; [:> rn/Image {:style  c-style/count-icon
     ;               :source assets/credential-record-detail}]]
     [:> rn/FlatList {:data          @(subscribe [:cert-list])
                      :render-item   (fn [^js item]
                                       (let [{:keys [type VIN] :as this-item} (-> item
                                                                                   .-item
                                                                                   t/->cljs)]
                                         (r/as-element
                                           [m-item navigation type VIN this-item])))
                      :key-extractor (fn [_ index]
                                       (str index))
                      :List-footer-component list-item/flat-list-footer
                      :shows-vertical-scroll-indicator false}]]))
