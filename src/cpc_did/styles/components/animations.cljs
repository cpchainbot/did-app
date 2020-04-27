(ns cpc-did.styles.components.animations
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.layout :as layout]))

(def loading {:width  size/w-180
              :height size/h-180})

;(def modal (merge layout/custom-modal
;                  {:justify-content "center"
;                   :align-items "center"}))
(def toast-loading-container {:margin-horizontal (size/h-margin 34)
                              :margin-vertical   (size/v-margin 24)})
;(def modal-loading-container {:flex 0
;                              :background-color "rgba(0,0,0,0.6)"
;                              :justify-content "center"
;                              :align-items "center"
;                              :width size/w-100
;                              :height size/h-80})
(def toast-loading {:width  size/w-40
                    :height size/h-40})