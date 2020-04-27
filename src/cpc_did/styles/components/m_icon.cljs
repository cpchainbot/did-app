(ns cpc-did.styles.components.m-icon
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(defn bottom-tab-label
  [focused]
  {:font-size size/bottom-tab-label
   :color (if focused
            color/bottom-tab-selected
            color/bottom-tab-normal)
   :margin-bottom (size/v-margin 10)
   :text-align "center"})

(def default-icon-style {:margin-top (size/v-margin 14)})
