(ns cpc-did.pages.qr-code.auth-failure
  (:require ["react-native" :as rn]
            ["expo-linear-gradient" :as lg]
            [reagent.core :as r]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.auth-result :as auth-res]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.utils.react-navigation :refer [nav-reset]]))

(defn page
  "docstring"
  [^js navigation state]
  [:> lg/LinearGradient {:style layout/container
                         :colors color/linear-colors
                         :start #js [0 0]
                         :end #js[0 1]}
   [:> rn/Image {:source assets/res-failure
                 :style  (merge layout/res-image
                                auth-res/res-image)}]
   [:> rn/Text {:style layout/res-title-shallow} "验证失败"]
   [:> rn/Text {:style layout/res-content-shallow} (:params state)]

   [:> rn/View {:style layout/auth-first-button}
    [m-button/start-page-button true "重新扫码" #(.navigate navigation "qr-code")]]
   [:> rn/View {:style auth-res/button-gap}
    [m-button/start-page-button false "回首页" #(nav-reset navigation "tab-nav")]]])
