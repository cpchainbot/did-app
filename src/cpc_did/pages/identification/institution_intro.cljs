(ns cpc-did.pages.identification.institution_intro
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            ["react-native-easy-toast" :default Toast :refer (DURATION)]
            [cpc-did.utils.http :as http]
            [re-frame.core :refer [subscribe dispatch]]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.institution-intro :as i-style]))

(defn page
  "docstring"
  [^js navigation state]
  (r/create-class
    {:component-did-mount (fn [this]
                            (http/request-wrapper http/GET_CLAIM (:params state)))
     :reagent-render (fn []
                       [:> rn/View {:style layout/container}
                        [:> rn/Image {:style i-style/image
                                      :source assets/logo-GAC-intro}]
                        [:> rn/Text {:style i-style/intro}
                         (:detail @(subscribe [:claim-detail]))]
                        [:> rn/View {:style i-style/button-container}
                         [m-button/normal-page-button true "获取认证ATTEST" #(.navigate navigation "ident-info")]]])
     :component-will-unmount (fn [this]
                               (dispatch [:add-claim-detail nil]))}))
