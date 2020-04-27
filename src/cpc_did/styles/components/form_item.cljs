(ns cpc-did.styles.components.form-item
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(def container {:flex 0
                :height size/h-77
                :width size/w-315})
(def blur-border {:border-color color/form-border-blur})
(def focus-border {:border-color color/form-border-focus})
(def err-border {:border-color color/form-border-error})

(def label {:font-size size/form-title
            :line-height size/text-line-height
            :color color/form-title-font})
(def input-box-container {:margin-top (size/v-margin 20)
                          :height size/h-31
                          :border-bottom-width size/form-border})

(def input-box {:flex-direction "row"
                :justify-content "space-between"
                :align-items "center"})
(def input {:color color/form-content-font
            :font-size size/form-hint
            :line-height size/text-line-height
            :width size/w-290})

(def label-container {:flex-direction "row"
                      :justify-content "space-between"})
