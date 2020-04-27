(ns cpc-did.styles.core
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :refer [raw-header-height]]))

(def container {:flex 1})
(def network-error-container {:position "absolute"
                              :z-index 1
                              :flex 0
                              :top raw-header-height
                              :left 0
                              :right 0
                              :height size/h-40
                              :justify-content "center"
                              :align-items "center"
                              :background-color "rgba(91, 163, 255, 0.5)"})
(def network-error {:font-size size/header-title
                    :color color/font-white})
