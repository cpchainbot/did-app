(ns cpc-did.styles.create-result
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(def avatar {:margin-top (size/v-margin 22)})
(def username {:margin-top (size/v-margin 10)
               :font-size  size/form-title
               :line-height size/text-line-height
               :color      color/font-deep})
(def id-container {:align-self  "flex-start"
                   :margin-top  (size/v-margin 28)
                   :margin-left (size/h-margin 30)})
(def id-title {:font-size size/list-title
               :line-height size/text-line-height
               :color     color/font-shallow})

(def id-content {:font-size  size/list-content
                 :color      color/font-deep
                 :line-height size/text-line-height
                 :margin-top (size/v-margin 8)})

(def intro-title {:font-size  size/intro-title
                  :color      color/intro-title
                  :line-height size/text-line-height
                  :margin-top (size/v-margin 48)})

(def intro-detail {:flex       0
                   :font-size  size/intro-info
                   :color      color/intro-info
                   :line-height size/text-line-height
                   :margin-top (size/v-margin 17)
                   :width      size/w-297})
(def backup-container {:margin-top (size/v-margin 32)})


