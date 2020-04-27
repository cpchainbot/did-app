(ns cpc-did.pages.user.auth-record-detail
  (:require ["react-native" :as rn]
            ["expo-linear-gradient" :as lg]
            [reagent.core :as r]
            [cpc-did.utils.date :as d]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.authorization-detail :as a-style]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.components.m-icon :as m-icon]
            [cpc-did.components.dash-line :refer [dash-line]]))

(defn page
  "docstring"
  [^js navigation state]
  (let [data (:params state)]
    [:> lg/LinearGradient {:style a-style/container
                           :colors color/linear-colors
                           :start #js [0 0]
                           :end #js [0 1]}
     [:> rn/View {:style a-style/header-container}
      [:> rn/TouchableOpacity {:style a-style/header-left
                               :on-press #(.goBack navigation)}
       [:> rn/Image {:source assets/arrow-back}]
       [:> rn/Text {:style a-style/title} "验证详情"]]
      [:> rn/View {:style a-style/more-func}
       [m-icon/normal-icon assets/more nil]]]
     [:> rn/View {:style a-style/image-container}
      [:> rn/Image {:source assets/authorization-background}]
      [:> rn/View {:style a-style/info-container}
       [:> rn/View {:style a-style/issuer-info}
        [:> rn/Text {:style (merge a-style/label {:margin-top 0})} "签发者"]
        [:> rn/Image {:source assets/logo-GAC-horizontal}]]
       [dash-line "100%" 65 "#979797"]
       [:> rn/View {:style a-style/detail}
        [:> rn/Text {:style (merge a-style/label {:margin-top 0})} "充电点位:"]
        [:> rn/Text {:style a-style/content} (:location data)]

        [:> rn/Text {:style a-style/label} "CPC ID:"]
        [:> rn/Text {:style a-style/content} (:verifier data)]

        [:> rn/Text {:style a-style/label} "状态:"]
        [:> rn/View {:style a-style/state-container}
         [:> rn/Image {:source assets/passed}]
         [:> rn/Text {:style a-style/state-text} (:state data)]]
        [:> rn/Text {:style a-style/label} "验证时间:"]
        [:> rn/Text {:style a-style/content} (d/to-time-string (:created data))]]]]]))
