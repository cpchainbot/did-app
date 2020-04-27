(ns cpc-did.styles.components.bottom-modal
  (:require [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]))

(def modal-container {:flex             1
                      :background-color color/modal-container-bg})
(def touchable {:flex 1})

(def content-container {:position         "absolute"
                        :left             0
                        :right            0
                        :bottom           0
                        :height           size/h-258
                        :justify-content  "flex-start"
                        :align-items      "center"
                        :background-color color/modal-content-bg
                        :border-top-left-radius size/bottom-modal-border-radius
                        :border-top-right-radius size/bottom-modal-border-radius})
(def modal-title {:margin-top (size/v-margin 32)
                  :font-size   size/header-title
                  :font-weight "bold"})

(def input-container {:margin-top          (size/v-margin 50)
                      :margin-bottom       (size/v-margin 40)
                      :width               size/w-315
                      :flex-direction      "row"
                      :justify-content     "space-between"
                      :border-bottom-width size/form-border})
(def input {:width size/w-315
            :margin-bottom (size/v-margin 5)})

(def toast-text {:margin-top (size/v-margin 16)
                 :font-size size/header-title
                 :color color/font-white
                 :font-weight "bold"})
