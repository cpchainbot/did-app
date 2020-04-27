(ns cpc-did.styles.components.camera
  (:require [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.components.header :as h-style]))

(def container {:flex 1
                :background-color "rgba(0,0,0,1)"})

(def layer-top {:flex             1
                :align-items "flex-start"
                :background-color color/camera-bg})

(def layer-center {:flex           0
                   :height         size/h-216
                   :flex-direction "row"})

(def layout-bottom {:flex             1
                    :background-color color/camera-bg})

(def center-left {:flex             1
                  :background-color color/camera-bg})
(def center-center {:flex            0
                    :width           size/w-216
                    :height          size/h-216
                    :justify-content "flex-start"
                    :align-items     "center"})
(def center-right {:flex             1
                   :background-color color/camera-bg})

(def header-left (merge h-style/back-header-left
                        {:flex 0
                         :margin-top size/status-bar-height
                         :height size/header-height}))
(def title (merge h-style/deep-bg-title
                  {:margin-left (size/h-margin 8)}))

(def hint-info {:margin-top (size/v-margin 53)
                :text-align "center"
                :font-size  size/hint-info
                :color      color/font-white})

;;; animation

(def base {:width            size/w-24
           :height           size/h-24
           :border-color     "#4664E2"
           :background-color "rgba(0,0,0,0)"
           :position         "absolute"})
(def left-top (merge
                base
                {:border-left-width size/qr-code-border
                 :border-top-width  size/qr-code-border
                 :left              0
                 :top               0}))
(def left-bottom (merge
                   base
                   {:border-left-width   size/qr-code-border
                    :border-bottom-width size/qr-code-border
                    :left                0
                    :bottom              0}))
(def right-top (merge
                 base
                 {:border-top-width   size/qr-code-border
                  :border-right-width size/qr-code-border
                  :right              0
                  :top                0}))
(def right-bottom (merge
                    base
                    {:border-right-width  size/qr-code-border
                     :border-bottom-width size/qr-code-border
                     :right               0
                     :bottom              0}))

(def ani-line-output [0 size/h-216 0])
