(ns cpc-did.components.header
  (:require ["react-native" :as rn]
            ["react-navigation" :as router :refer (NavigationActions)]
            [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.components.header :as h-style]
            [cpc-did.components.static-assets :as assets]))

(defn stack-page-left
  "A custom left header for stack page."
  [^js navigation title back-func]
  [:> rn/TouchableOpacity {:on-press (if-let [{:keys [method route-name]} back-func]
                                       (case method
                                         "reset" #(.reset navigation #js [(.navigate ^js NavigationActions #js {:routeName route-name})] 0)
                                         "pop" #(.pop navigation)
                                         nil)
                                       #(.goBack navigation))
                           :style    h-style/sp-header-left-container}
   [:> rn/Image {:source assets/arrow-back-black}]
   [:> rn/Text {:style h-style/header-left-title} title]])

(defn tab-page-left
  "For bottom tab page"
  [title]
  [:> rn/Text {:style h-style/header-left-title} title])

(defn header-right
  [on-press]
  [:> rn/TouchableOpacity {:on-press on-press}
   [:> rn/Image {:source assets/more-black}]])

