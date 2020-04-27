(ns cpc-did.pages.qr-code.auth-success
  (:require ["react-native" :as rn :refer (BackHandler)]
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
  (let [on-back (fn []
                  (nav-reset navigation "tab-nav")
                  true)]
    (r/create-class
      {:component-did-mount (fn [this]
                              (.addEventListener ^js BackHandler
                                                 "hardwareBackPress"
                                                 on-back))
       :reagent-render (fn []
                         [:> lg/LinearGradient {:style layout/container
                                                :colors color/linear-colors
                                                :start #js [0 0]
                                                :end #js[0 1]}
                          [:> rn/Image {:source assets/res-success
                                        :style  layout/res-image}]
                          [:> rn/Text {:style layout/res-title-shallow} "通过验证"]
                          [:> rn/Text {:style layout/res-content-shallow} "请放心使用充电桩"]

                          [:> rn/View {:style layout/auth-first-button}
                           [m-button/start-page-button true "查看验证详情" #(nav-reset navigation "auth-detail" (:params state))]]
                          [:> rn/View {:style auth-res/button-gap}
                           [m-button/start-page-button false "返回首页 " #(nav-reset navigation "tab-nav")]]])
       :component-will-unmount (fn [this]
                                 (.removeEventListener ^js BackHandler "hardwareBackPress" on-back))})))



