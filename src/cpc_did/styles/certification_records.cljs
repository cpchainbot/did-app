(ns cpc-did.styles.certification-records
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.layout :as layout]))
(def hm-24 (size/h-margin 24))
(def vm-16 (size/v-margin 16))
(def custom-options (merge layout/shadow-options
                           {:height size/h-100}))
(def i-container (merge layout/shadow-content
                        {:height size/h-100}))
(def i-content {:flex 1
                :justify-content "center"
                :padding-horizontal (size/h-margin 16)})

(def info-container {:margin-top (size/v-margin 10)})

(def count-container {:flex             0
                      :height           size/h-64
                      :flex-direction   "row"
                      :align-items      "center"
                      :justify-content  "space-between"
                      :background-color "#F2F2F5"})
(def count-text {:margin-left hm-24
                 :font-size   size/normal-text
                 :font-weight "bold"
                 :color       "#8A8F9C"})

(def count-icon {:margin-right hm-24})
