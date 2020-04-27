(ns cpc-did.styles.components.list-item
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.layout :as layout]))


(def two-item-container {:flex-direction  "row"
                         :align-items     "center"
                         :justify-content "space-between"})
(def list-title-container {:flex-direction  "row"
                           :justify-content "flex-start"
                           :align-items     "center"})
(def title-style {:margin-left (size/h-margin 8)})

(defn person-info-container
  [margin-top]
  {:margin-top      (size/v-margin margin-top)
   :flex-direction  "row"
   :align-items     "flex-start"
   :justify-content "flex-start"})

(def icon {:margin-top (size/v-margin 3)})
(def label {:margin-left (size/h-margin 8)})
(def label-title {:font-size   size/normal-text
                  :line-height size/text-line-height
                  :color       color/font-white})
(def info {:margin-top  (size/v-margin 5)
           :font-size   size/normal-text
           :line-height size/text-line-height
           :color       color/font-white
           :font-weight "bold"})

(def flat-list-footer {:margin-top (size/v-margin 30)})
(def flat-list-empty (merge layout/container-center
                            {:align-content "center"
                             :width size/window-width}))

(def two-item-left-container {:flex-direction  "row"
                              :align-items     "center"
                              :justify-content "flex-start"})