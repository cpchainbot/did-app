(ns cpc-did.styles.identification-main
  (:require [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.layout :as layout]))

(def custom-options (merge layout/shadow-options
                           {:height size/h-118}))

(def list-container (merge layout/shadow-content
                           {:height size/h-118}))
(def list-content {:flex 1
                   :justify-content "center"
                   :padding-horizontal (size/h-margin 16)})

(def detail-container {:margin-top (size/v-margin 16)
                       :flex-direction "row"
                       :align-items "flex-start"
                       :justify-content "space-between"})
(def detail-text {:font-size size/intro-info
                  :line-height size/text-line-height
                  :color color/font-shallow
                  :width size/w-194})
