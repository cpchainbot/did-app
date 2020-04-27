(ns cpc-did.styles.certification-detail
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))


(def scroll-view {:background-color color/background
                  :width            "100%"})

(def info-container {:margin-top       2
                     :justify-content  "center"
                     :align-items      "center"
                     :background-color color/background-white})
(def image-container {:flex     0
                      :position "relative"})

(def info-content-container {:position        "absolute"
                             :top             0
                             :right           0
                             :bottom          0
                             :left            0
                             :justify-content "center"})
(def info-content {:flex        0
                   :margin-left (size/h-margin 36)})


(def detail-container {:margin-top  (size/v-margin 32)
                       :background-color color/background-white})
(def detail {:margin-left (size/h-margin 30)
             :margin-top (size/v-margin 20)})

(def group-margin {:margin-top (size/v-margin 24)})
(def item-margin {:margin-top (size/v-margin 8)})

(def logo-margin {:margin-top (size/v-margin 12)})

(def state-container (merge item-margin
                            {:flex-direction "row"
                             :align-items    "center"}))

(def text-image-margin {:margin-left (size/h-margin 4)})
(def footer {:margin-top    (size/v-margin 44)
             :margin-bottom (size/v-margin 24)
             :font-size     17
             :font-weight   "bold"
             :color         "#DEDEDE"
             :align-self    "center"})
