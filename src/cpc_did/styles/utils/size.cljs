(ns cpc-did.styles.utils.size
  (:require ["react-native" :refer (StatusBar Platform)]
            ["react-navigation-stack" :refer (Header)]
            [cpc-did.utils.size-scale :as s-scale]))

;;; Platform
(def ios? (= (.-OS Platform) "ios"))
(def android? (= (.-OS Platform) "android"))

;;; header relevant
(def raw-header-height (.-HEIGHT ^js Header))
(def status-bar-height (if ios?
                         20
                         (.-currentHeight StatusBar)))
(def header-height (if ios?
                     (- raw-header-height status-bar-height)
                     raw-header-height))
(defn get-height-contain-header
  [height]
  (s-scale/get-height (+ height status-bar-height)))

;;; window size
;;; reference: https://stackoverflow.com/questions/44978804/whats-the-difference-between-window-and-screen-in-the-dimensions-api
(def window-height (s-scale/get-window-size :height))
(def window-width (s-scale/get-window-size :width))

;;; start page
(def app-name 22)
(def footer 14)

;;; button relevant
(def start-page-button-width (s-scale/get-width 296))
(def small-page-button-width (s-scale/get-width 255))
(def normal-page-button-width (s-scale/get-width 315))
(def button-height (s-scale/get-height 50))
(def button-border 1)
(def button-radius 4)
(def button-font 16)

;;; form relevant
(def form-title 17)
(def form-hint 16)
(def form-border 2)

;;; header relevant
(def header-title 20)

;;; introduction information
(def intro-title 18)
(def intro-info 16)

;;; text hint information
(def hint-text 13)
(def hint-button 14)

;;; list relevant
(def list-title 17)
(def list-content 14)

;;; bottom tab relevant
(def bottom-tab-height (s-scale/get-height 66))
(def bottom-tab-label 12)

;;; line height
(def text-line-height 24)
(def help-text-line-height 22)

;;; border radius
(def bottom-modal-border-radius 12)
(def alert-modal-border-radius 4)
(def list-border-radius 2)

;;; others
(def normal-text 16)
(def hint-info 18)
(def normal-title 18)
(def cpc-id 12)

(def qr-code-border 4)


;;; some width size
(def h-4 (s-scale/get-height 4))
(def h-24 (s-scale/get-height 24))
(def h-40 (s-scale/get-height 40))
(def h-60 (s-scale/get-height 60))
(def h-64 (s-scale/get-height 64))
(def h-31 (s-scale/get-height 31))
(def h-77 (s-scale/get-height 77))
(def h-88 (s-scale/get-height 88))
(def h-100 (s-scale/get-height 100))
(def h-110 (s-scale/get-height 110))
(def h-118 (s-scale/get-height 118))
(def h-120 (s-scale/get-height 120))
(def h-124 (s-scale/get-height 124))
(def h-180 (s-scale/get-height 180))
(def h-216 (s-scale/get-height 216))
(def h-238 (s-scale/get-height 238))
(def h-241 (s-scale/get-height 241))
(def h-258 (s-scale/get-height 258))
(def h-281 (s-scale/get-height 281))
(def h-310 (s-scale/get-height 310))
(def h-348 (s-scale/get-height 348))


;;; some height size
(def w-24 (s-scale/get-width 24))
(def w-40 (s-scale/get-width 40))
(def w-100 (s-scale/get-width 100))
(def w-168 (s-scale/get-width 168))
(def w-180 (s-scale/get-width 180))
(def w-194 (s-scale/get-width 194))
(def w-216 (s-scale/get-width 216))
(def w-250 (s-scale/get-width 250))
(def w-256 (s-scale/get-width 256))
(def w-290 (s-scale/get-width 290))
(def w-297 (s-scale/get-width 297))
(def w-299 (s-scale/get-width 299))
(def w-313 (s-scale/get-width 313))
(def w-314 (s-scale/get-width 314))
(def w-315 (s-scale/get-width 315))
(def w-327 (s-scale/get-width 327))

;;; some functions to use properties with multiplied value
(defn border-width
  [t & rest]
  (case (count rest)
    0 {:border-width t}
    1 {:border-top-width t
       :border-right-width (first rest)
       :border-bottom-width t
       :border-left-width (first rest)}
    2 {:border-top-width t
       :border-right-width (first rest)
       :border-bottom-width (nth rest 1)
       :border-left-width (first rest)}
    3 {:border-top-width t
       :border-right-width (first rest)
       :border-bottom-width (nth rest 1)
       :border-left-width (nth rest 2)}
    (throw (js/Error "A bad usage of border"))))

(defn v-margin
  [v]
  (s-scale/get-height v))

(defn h-margin
  [h]
  (s-scale/get-width h))

(defn margin
  [t & rest]
  (case (count rest)
    0 {:margin-vertical (s-scale/get-height t)
       :margin-horizontal (s-scale/get-width t)}
    1 {:margin-vertical (s-scale/get-height t)
       :margin-horizontal (s-scale/get-width (first rest))}
    2 {:margin-top (s-scale/get-height t)
       :margin-horizontal (s-scale/get-width (first rest))
       :margin-bottom (s-scale/get-height (nth rest 1))}
    3 {:margin-top (s-scale/get-height t)
       :margin-right (s-scale/get-width (first rest))
       :margin-bottom (s-scale/get-height (nth rest 1))
       :margin-left (s-scale/get-width (nth rest 2))}
    (throw (js/Error "A bad usage of margin"))))
