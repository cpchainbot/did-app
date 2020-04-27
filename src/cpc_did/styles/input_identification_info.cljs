(ns cpc-did.styles.input-identification-info
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(def intro-container {:align-items "center"
                      :margin-top (size/v-margin 24)})

(def hint-info {:flex 0
                :margin-top  (size/v-margin 12)
                :font-size   size/normal-text
                :color       color/font-shallow
                :line-height size/text-line-height
                :width       size/w-315})
(def first-item {:margin-top (size/v-margin 26)})
(def other-item {:margin-top (size/v-margin 20)})

(def button-container {:margin-top (size/v-margin 40)})

(def ani-container {:margin-top (size/v-margin 163)})

(def modal-title-base {:font-size size/hint-info
                       :color     color/font-deep})
(def modal-content-base {:font-size 13
                         :color     color/font-shallow})
(def loading-modal-title (merge modal-title-base
                                {:margin-top (size/v-margin 31)}))
(def loading-modal-content (merge modal-content-base
                                  {:margin-top (size/v-margin 16)}))
(def modal-container {:position "absolute"
                      :top 0
                      :left 0
                      :bottom 0
                      :right 0})

(def extra-height {:extra-height size/h-124})