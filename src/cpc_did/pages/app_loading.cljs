(ns cpc-did.pages.app-loading
  (:require ["expo" :as expo]
            [reagent.core :as r]
            [cpc-did.event]
            [cpc-did.subs]
            [cpc-did.db :refer [storage->items]]
            [cpc-did.styles.utils.layout :as layout]
            [re-frame.core :refer [dispatch-sync]]
            [cpc-did.utils.http :as http]
            [cpc-did.utils.cljs-js-transit :as t])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn page
  "docstring"
  [^js navigation state]
  (r/create-class
    {:component-did-mount (fn [this]
                            (dispatch-sync [:initialise])
                            (promise-> #(println %)
                                       (storage->items)
                                       (fn [route-name]
                                         (http/request-wrapper http/LOADING_GET_CLAIM_INDEX nil)
                                         route-name)
                                       (fn [route-name]
                                         (js/setTimeout #(.navigate navigation route-name) 200))))

     :reagent-render (fn []
                       [:> expo/AppLoading
                        {:auto-hide-splash true}])}))
