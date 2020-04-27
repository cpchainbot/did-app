(ns cpc-did.styles.create-id
  (:require [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]))

(def first-form-item {:margin-top (size/v-margin 83)})
(def other-form-item {:margin-top (size/v-margin 28)})

(def button-container {:flex 0
                       :margin-top (size/v-margin 80)
                       :justify-content "center"
                       :align-items "center"})
(def ani-container {:margin-top (size/v-margin 108)})

(def intro-container {:flex        0
                      :margin-top  (size/v-margin 88)
                      :width       size/w-297
                      :align-items "center"})
(def intro-title {:font-size size/hint-info
                  :line-height size/text-line-height
                  :color     color/font-deep})
(def intro-detail {:font-size  size/normal-text
                   :color      color/font-shallow
                   :line-height size/text-line-height
                   :margin-top (size/v-margin 17)})
(def extra-height {:extra-height size/h-60})
