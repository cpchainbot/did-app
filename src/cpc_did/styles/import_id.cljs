(ns cpc-did.styles.import-id
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(def hint-container {:flex       0
                     :width      size/w-315
                     :margin-top (size/v-margin 40)})

(def hint {:font-size size/form-title
           :color     color/form-title-font
           :line-height size/text-line-height})

(def keystore {:font-size   size/form-hint
               :color       color/font-deep
               :line-height size/text-line-height
               :text-align-vertical "top"
               :height      "100%"
               :width       "100%"})

(def form-item {:margin-top (size/v-margin 31)})
(def button-container {:margin-top      (size/v-margin 40)
                       :justify-content "center"
                       :align-items     "center"})

(def extra-height size/h-110)