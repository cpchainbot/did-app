(ns cpc-did.styles.authorization-detail
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.components.header :as h-style]))

(def container {:flex            1
                :justify-content "flex-start"
                :align-items     "center"})

(def header-container (merge h-style/header-container
                             {:width size/window-width}))

(def header-left h-style/back-header-left)
(def title (merge h-style/deep-bg-title
                  {:margin-left (size/h-margin 4)}))

(def more-func h-style/header-right)

(def image-container {:flex     0
                      :position "relative"
                      :margin-top (size/v-margin 65)})

(def info-container {:position        "absolute"
                     :top             0
                     :right           0
                     :bottom          0
                     :left            0})

(def issuer-info {:height size/h-88
                  :margin-horizontal (size/h-margin 22)
                  :flex-direction "row"
                  :align-items "center"
                  :justify-content "space-between"})

(def detail {:margin-top  (size/v-margin 31)
             :margin-horizontal (size/h-margin 21)})

(def label (merge layout/item-label
                  {:margin-top (size/v-margin 16)}))

(def content (merge layout/item-content
                    {:margin-top (size/v-margin 4)}))

(def state-container {:flex-direction "row"
                      :align-items    "center"})

(def state-text (merge content {:margin-left (size/h-margin 4)}))

