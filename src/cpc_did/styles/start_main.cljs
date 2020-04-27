(ns cpc-did.styles.start-main
  (:require [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))
(def container {:flex 1})
(def linear-colors #js [color/background-shallow color/background-deep])

(def logo-background {:position "absolute"
                      :top      0
                      :right    0})
(def logo-cpc {:margin-top (size/v-margin 166)
               :margin-left (size/h-margin 38)})
(def text-container {:margin-top (size/v-margin 18)
                     :margin-left (size/h-margin 39)})
(def title-container {:flex-direction  "row"
                      :justify-content "flex-start"})
(def title-bold {:font-size   size/app-name
                 :font-weight "bold"
                 :color color/app-info})
(def title-normal {:font-size size/app-name
                   :color color/app-info
                   :margin-left (size/h-margin 3)})

(def normal-text {:font-size size/footer
                  :color color/app-info})

(def button-container {:height     size/h-124
                       :margin-top (size/v-margin 120)
                       :align-items "center"
                       :justify-content "space-between"})
