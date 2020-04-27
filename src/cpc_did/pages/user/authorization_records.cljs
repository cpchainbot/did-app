(ns cpc-did.pages.user.authorization-records
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            ["react-native-shadow" :refer (BoxShadow)]
            [cpc-did.utils.date :as d]
            [re-frame.core :refer [subscribe]]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.styles.authorization-records :as a-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.utils.cljs-js-transit :as t]))

(defn m-item
  [^js navigation title supplier created item]
  [:> BoxShadow {:setting a-style/custom-options}
   [:> rn/View {:style a-style/i-container}
    [:> rn/TouchableOpacity {:style    a-style/i-content
                             :on-press #(.navigate navigation "auth-record-detail" item)}
     [list-item/three-item assets/list-car title layout/list-title assets/arrow-enter]
     [:> rn/View {:style a-style/detail-container}
      [:> rn/Text {:style layout/label} "验证方："]
      [:> rn/Text {:style layout/content} supplier]
      [:> rn/Text {:style a-style/m-time} (d/to-time-string created)]]]]])

(defn page
  "docstring"
  [navigation state]
  (r/as-element
    [:> rn/View {:style layout/cross-axis-container}
     ;[:> rn/View {:style a-style/count-container}
     ; [:> rn/Text {:style a-style/count-text} "共" @(subscribe [:auth-count]) "条认证"]
     ; [:> rn/Image {:style  a-style/count-icon
     ;               :source assets/auth-record-detail}]]
     [:> rn/FlatList {:data          @(subscribe [:auth-list])
                      :render-item   (fn [^js item]
                                       (let [{:keys [type supplier created] :as this-item} (-> item
                                                                                               .-item
                                                                                               t/->cljs)]
                                         (r/as-element
                                           [m-item navigation type supplier created this-item])))
                      :key-extractor (fn [_ index]
                                       (str index))
                      :List-footer-component list-item/flat-list-footer
                      :shows-vertical-scroll-indicator false}]]))
