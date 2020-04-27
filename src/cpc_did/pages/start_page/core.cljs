(ns cpc-did.pages.start-page.core
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            ["expo-linear-gradient" :as lg]
            [cpc-did.styles.start-main :as s-main]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.components.static-assets :as assets]))


(defn page
  "Start main page, give a select for create or import ID"
  [^js navigation state]
  (fn []
    [:> lg/LinearGradient {:style s-main/container
                           :colors s-main/linear-colors
                           :start #js [0 0]
                           :end #js [0 1]}
     [:> rn/Image {:style s-main/logo-background
                   :source assets/logo-background}]
     [:> rn/Image {:style s-main/logo-cpc
                   :source assets/logo-cpc}]
     [:> rn/View {:style s-main/text-container}
      [:> rn/View {:style s-main/title-container}
       [:> rn/Text {:style s-main/title-bold} "CPC ID"]
       [:> rn/Text {:style s-main/title-normal} "Certification"]]
      [:> rn/Text {:style s-main/normal-text} "Powered by CYBER PHYSICAL CHAIN"]]
     [:> rn/View {:style s-main/button-container}
      [m-button/start-page-button true "创建CPC ID" #(.navigate navigation "create-id")]
      [m-button/start-page-button false "导入CPC ID"#(.navigate navigation "import-id")]]]))
