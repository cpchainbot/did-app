(ns cpc-did.styles.backup-id
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.layout :as layout]))

(def hint {:flex 0
           :margin-top (size/v-margin 20)
           :width size/w-313
           :font-size size/normal-text
           :line-height size/text-line-height
           :color color/font-deep})

(def keystore-container {:flex 0
                         :width size/w-314
                         :height size/h-238
                         :margin-top (size/v-margin 24)
                         :background-color "#F3F6FA"
                         :justify-content "center"
                         :align-items "center"})
(def keystore {:flex 0
               :color color/font-deep
               :font-size size/intro-info
               :line-height size/text-line-height})

(def hint-text {:margin-left (size/h-margin 4)
                :font-size size/intro-info
                :color color/button-text-font})

(def button-container {:margin-top (size/v-margin 68)})


(def modal-container (merge layout/alert-modal-container
                            {:width size/w-299
                             :height size/h-348}))
;(def modal-image {:margin-top (size/v-margin 24)})
(def modal-title {:margin-top (size/v-margin 24)
                  :font-size size/header-title
                  :color color/header-title-font-deep
                  :font-weight "bold"})
(def modal-content {:width size/w-256
                    :margin-top (size/v-margin 8)
                    :line-height size/text-line-height
                    :font-size size/normal-text
                    :color color/alert-hint-info
                    :text-align "center"})
(def modal-button-container {:margin-top (size/v-margin 44)})
