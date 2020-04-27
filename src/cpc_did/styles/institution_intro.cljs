(ns cpc-did.styles.institution-intro
  (:require [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]))

(def image {:margin-top (size/v-margin 32)})

(def intro {:margin-top (size/v-margin 37)
            :width size/w-297
            :height size/h-281
            :color color/font-shallow
            :font-size size/normal-text
            :line-height size/text-line-height})
(def button-container {:margin-top (size/v-margin 4)})
