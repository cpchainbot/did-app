(ns cpc-did.styles.components.button
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(def base {:height          size/button-height
           :border-radius   size/button-radius
           :border-width    size/button-border
           :justify-content "center"
           :align-items     "center"})

(defn title
  [state]
  {:font-weight "bold"
   :font-size   size/normal-text
   :color       (case state
                  "selected" color/button-selected-font
                  "normal" color/button-normal-font
                  "untouchable" color/button-untouchable-font
                  nil)})

(defn start-page-button
  [is-selected]
  (merge
    base
    {:flex             0
     :width            size/start-page-button-width
     :border-color     color/deep-bd-border
     :background-color (if is-selected
                         color/button-selected-bg
                         color/button-normal-bg)}))

(def normal-page-button (merge
                          base
                          {:flex             0
                           :width            size/normal-page-button-width
                           :background-color color/button-normal-bg
                           :border-color     color/shallow-bg-border}))

(def small-page-button (merge
                           base
                           {:flex 0
                            :width size/small-page-button-width
                            :background-color color/button-normal-bg
                            :border-color color/shallow-bg-border}))

(def hint-button-container {:flex 0
                            :align-items "flex-start"})
(defn hint-button-content
  [margin-top]
  {:flex 0
   :margin-top     (size/v-margin margin-top)
   :flex-direction "row"
   :align-items "center"
   :justify-content "flex-start"})
(def hint {:margin-left (size/h-margin 4)
           :font-size size/intro-info
           :color color/theme-color})

(defn text-button-title
  [margin-top]
  {:margin-top (size/v-margin margin-top)})
