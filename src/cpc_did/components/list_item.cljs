(ns cpc-did.components.list-item
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            [cpc-did.styles.components.list-item :as list-item]
            [cpc-did.styles.utils.size :as size]))

(defn two-item
  "Two item"
  [title title-style icon]
  [:> rn/View {:style list-item/two-item-container}
   [:> rn/Text {:style title-style} title]
   [:> rn/Image {:source icon}]])

(defn three-item
  [left-icon title title-style right-icon]
  [:> rn/View {:style list-item/two-item-container}
   [:> rn/View {:style list-item/list-title-container}
    [:> rn/Image {:source left-icon}]
    [:> rn/Text {:style (merge title-style list-item/title-style)} title]]
   [:> rn/Image {:source right-icon}]])

(defn person-info-item
  [source title info margin-top]
  [:> rn/View {:style (list-item/person-info-container margin-top)}
   [:> rn/Image {:source source
                 :style list-item/icon}]
   [:> rn/View {:style list-item/label}
    [:> rn/Text {:style list-item/label-title} title]
    [:> rn/Text {:style list-item/info} info]]])

(defn two-item-left
  [icon title title-style]
  [:> rn/View {:style list-item/two-item-left-container}
   [:> rn/Image {:source icon}]
   [:> rn/Text {:style (merge title-style
                              list-item/title-style)} title]])

(defn flat-list-footer
  []
  (r/as-element
    [:> rn/View {:style list-item/flat-list-footer}]))

(defn flat-list-empty
  [height]
  (r/as-element
    [:> rn/View {:style (merge list-item/flat-list-empty
                               {:height (- @height (size/v-margin 30))})}
     [:> rn/Text (if (zero? @height) "" "列表为空，下拉刷新")]]))
