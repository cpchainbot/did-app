(ns cpc-did.styles.authorize
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.layout :as layout]))

(def custom-options (merge layout/shadow-options
                           {:height size/h-100}))

(def i-container (merge layout/shadow-content
                        {:height size/h-100
                         :padding-horizontal (size/h-margin 16)
                         :flex-direction "row"
                         :align-items "center"}))

(def i-content {:flex            1
                :flex-direction  "row"
                :align-items     "center"
                :justify-content "space-between"})

(def info-container {:flex 0
                     :align-items     "flex-start"})

(def list-detail-container {:margin-top (size/v-margin 10)})

(def alert-modal-container (merge layout/alert-modal-container
                                  {:width           size/w-299
                                   :padding-bottom (size/v-margin 24)
                                   :justify-content "flex-start"}))
(def close-icon-container {:flex        0
                           :align-self  "flex-start"
                           :margin-top  (size/v-margin 2)
                           :margin-left (size/h-margin 2)
                           :padding     10})
(def alert-info-container {:flex  0
                           :width size/small-page-button-width})
(def item-label (merge layout/item-label
                       {:margin-top (size/v-margin 16)}))
(def item-content (merge layout/item-content
                         {:margin-top  (size/v-margin 4)
                          :font-weight "bold"}))

(def alert-hint-info {:font-size     size/list-content
                      :color         color/font-shallow
                      :line-height   size/text-line-height
                      :margin-top    (size/v-margin 25)
                      :margin-bottom (size/v-margin 20)})

(def verify-box-container {:position         "absolute"
                           :bottom           0
                           :left             0
                           :right            0
                           :height           size/h-88
                           :background-color color/background-white})
(def verify-box {:width "100%"
                 :height "100%"
                 :justify-content "center"
                 :align-items "center"})
(def shadow-options {:width size/window-width
                     :color "#000"
                     :opacity 0.1
                     :x 0
                     :y 0
                     :border 5
                     :side "top"})
(def bottom-placeholder {:flex 0
                         :height size/h-88
                         :width "100%"})

(def ani-container {:margin-top (size/v-margin 163)})

(def modal-title-base {:font-size size/hint-info
                       :color     color/font-deep})
(def modal-content-base {:font-size 13
                         :color     color/font-shallow})
(def loading-modal-title (merge modal-title-base
                                {:margin-top (size/v-margin 31)}))
(def loading-modal-content (merge modal-content-base
                                  {:margin-top (size/v-margin 16)}))

