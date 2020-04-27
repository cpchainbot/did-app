(ns cpc-did.styles.user-main
  (:require [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.components.header :as h-style]))

(def container {:flex 1
                :background-color color/background})
(def header-info-container {:flex   0
                            :height (size/get-height-contain-header 206)
                            :justify-content "space-between"})

(def header-container h-style/header-container)
(def title (merge h-style/deep-bg-title
                  {:margin-left (size/h-margin 16)}))

(def more-func h-style/header-right)

(def user-info {:flex 1
                :align-items "center"
                :justify-content "space-between"
                :margin-top (size/v-margin 10)
                :margin-bottom (size/v-margin 12)})

(def user-info-text {:flex 0
                     :justify-content "flex-start"})
(def avatar-info {:flex 0
                  :align-items "center"})
(def user-name {:font-size size/header-title
                :color     color/font-white})
(def user-cpc {:font-size  size/cpc-id
               :color      color/font-white
               :margin-top (size/v-margin 14)})
(def user-cpc-content {:font-size  size/cpc-id
                       :color      "rgba(255, 255, 255, 0.55)"
                       :margin-top (size/v-margin 4)})

(def list-tile {:font-size size/list-title
                :color     color/list-title})
(def list-container {:margin-right (size/h-margin 32)})
(defn m-list-item
  [has_border]
  (merge
    {:height              size/h-64
     :margin-left         (size/h-margin 24)
     :justify-content     "center"}
    (if has_border
      {:border-bottom-width 1
       :border-bottom-color color/list-border}
      {})))

(def m-list-item-touchable {:flex             0
                            :background-color color/button-selected-bg})

(def first-group (merge (size/border-width 1 0) {:border-color color/list-border}))
(def second-group (merge first-group {:margin-top (size/v-margin 10)}))
