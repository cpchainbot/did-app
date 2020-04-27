(ns cpc-did.components.m-icon
  (:require ["react-native" :as rn]
            [cpc-did.styles.components.m-icon :as m-styles]
            [reagent.core :as r]
            [cpc-did.styles.components.header :as h-styles]
            [cpc-did.styles.components.list-item :as list-item-styles]))



(defn bottom-tab-item [focused select-image normal-image style]
  (r/as-element
    [:> rn/View {:style (if style style m-styles/default-icon-style)}
     [:> rn/Image {:source (if focused select-image normal-image)}]]))

(defn bottom-tab-label
  [focused title]
  (r/as-element
    [:> rn/Text {:style (m-styles/bottom-tab-label focused)} title]))

(defn back-end-icon
  [source on-press]
  [:> rn/TouchableOpacity {:on-press on-press}
   [:> rn/Image {:source source}]])

(defn normal-icon
  [source on-press]
  [:> rn/TouchableOpacity {:on-press on-press}
   [:> rn/Image {:source source}]])
;(defn header-item [on-press source]
;  (r/as-element
;    [:> rn/TouchableOpacity {:on-press on-press
;                             :active-opacity 0.6 }
;     [:> rn/Image {:style h-styles/header-icon
;                   :source source}]]))

(defn text-input-icon
  [source on-press]
  [:> rn/TouchableWithoutFeedback {:on-press on-press
                                   :style {:position "absolute"
                                           :right 0
                                           :bottom 0}}
   [:> rn/Image {:source source}]])


