(ns cpc-did.styles.utils.layout
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

;;; container relevant
(def flex-0 {:flex 0})
(def flex-1 {:flex 1})
(def container (merge flex-1
                      {:align-items "center"}))

(def container-center (merge
                        container
                        {:justify-content "center"}))

(def background {:background-color color/background})
(def cross-axis-container (merge background container))
(def main-axis-container (merge background flex-1 {:justify-content "center"}))
(def gb-container-center (merge background container-center))

;;; bottom tab relevant
(def qr-tab-style {:margin-bottom (size/v-margin 18)})

(def hint-info {:flex      0
                :font-size size/normal-text
                :color     color/hint-info})

(def toast {:opacity 0.6})

;;; helper stack
(def help-text {:font-size   size/normal-text
                :color       color/font-deep
                :line-height size/help-text-line-height
                :width       size/w-313
                :margin-top (size/v-margin 16)})

;;; authorization and identification result relevant

(def res-image {:margin-top (size/v-margin 139)})
(def res-title-base {:font-size   size/hint-info
                     ::margin-top (size/v-margin 24)})
(def res-title-deep (merge res-title-base {:color color/font-deep}))
(def res-title-shallow (merge res-title-base {:color color/font-white}))

(def res-content-base {:font-size  size/hint-text
                       :margin-top (size/v-margin 16)})
(def res-content-deep (merge res-content-base {:color color/font-shallow}))
(def res-content-shallow (merge res-content-base {:color color/font-white}))

(def ident-first-button {:flex       0
                         :margin-top (size/v-margin 210)})
(def auth-first-button {:flex       0
                        :margin-top (size/v-margin 153)})

;;; keyboard scroll
(def extra-height {:extra-height size/h-110})
(def keyboard-scroll-options {:enable-automatic-scroll true
                              :enable-on-android true
                              :shows-vertical-scroll-indicator false})

;;; keystore relevant
(def placeholder-color color/form-hint-font)
(def keystore-container {:flex             0
                         :margin-top       (size/v-margin 16)
                         :width            size/w-315
                         :height           size/h-241
                         :border-width     1
                         :padding-horizontal (size/h-margin 11)
                         :padding-vertical (size/v-margin 9)
                         :border-color     "rgba(0, 0, 0, 0.1)"
                         :background-color color/background-white})

;;; shadow relevant
;; for BoxShadow, and the
;; height is custom
;; FlatList will affect shadow, so we need add a margin
(def shadow-options {:width size/w-327
                     :color "#000"
                     :opacity 0.05
                     :border 5
                     :radius 5
                     :x 0
                     :y 0
                     :style (merge (size/margin 24 5 5)
                                   {:justify-content "center"
                                    :align-items "center"})})
(def shadow-content {:width size/w-327
                     :background-color color/background-white
                     :border-radius 5})

;;; list relevant
;; for identification, record page
(def list-title {:font-size size/list-title
                 :font-weight "bold"
                 :line-height size/text-line-height
                 :color color/list-title})
(def label {:font-size size/list-content
            :color     color/list-detail
            :line-height size/text-line-height})
(def content {:font-size   size/list-content
              :font-weight "bold"
              :line-height size/text-line-height
              :color       color/list-title})

;;; item relevant
(def item-label {:font-size   size/normal-text
                 :color       color/font-shallow
                 :line-height size/text-line-height})
(def item-content {:font-size   size/normal-text
                   :color       color/font-deep
                   :line-height size/text-line-height})

;;; Modal relevant

(def transparent-modal-background {:flex 1
                                   :justify-content "center"
                                   :align-items "center"
                                   :background-color color/modal-container-bg})
(def alert-modal-container {:flex 0
                            :align-items "center"
                            :justify-content "center"
                            :background-color color/modal-content-bg
                            :border-radius size/alert-modal-border-radius})
