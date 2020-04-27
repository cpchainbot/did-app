(ns cpc-did.styles.components.header
  (:require [clojure.string :as c-str]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.styles.utils.size :as size]))

;;; A transformation function

(defn first-to-lower
  "Convert first character of s to lower case"
  [s]
  (str (-> s (subs 0 1) .toLowerCase) (subs s 1)))
(defn hyphens-to-camel
  "Convert styles key to camel format"
  [styles]
  (let [ks (keys styles)]
    (into {}
          (for [k ks]
            (assoc {}
              (-> k
                  name
                  (c-str/split #"-")
                  (->> (map c-str/capitalize))
                  (c-str/join)
                  first-to-lower
                  keyword)
              (get styles k))))))

;;; styles
(def string-header-title-style
  ;; The styles pass to React style function without reagent transform, so
  ;; must use a camel format.
  (hyphens-to-camel {:color       color/header-title-font-deep
                     :font-size   size/header-title
                     :font-weight "bold"}))

(def header-left-container-style
  (hyphens-to-camel {:padding-left (size/h-margin 16)}))

(def header-right-container-style
  (hyphens-to-camel {:padding-right (size/h-margin 16)}))

(def sp-header-left-container {:flex-direction "row"
                               :align-items    "center"})

(def header-left-title {:font-size   size/header-title
                        :font-weight "bold"
                        :color       color/header-title-font-deep
                        :margin-left (size/h-margin 8)})
;;; custom header
(def header-container {:flex-direction  "row"
                       :align-items     "center"
                       :justify-content "space-between"
                       :height          size/header-height
                       :margin-top      size/status-bar-height})

(def back-header-left {:margin-left    (size/h-margin 16)
                       :flex-direction "row"
                       :align-items    "center"})
(def header-right {:margin-right (size/h-margin 16)})
(def deep-bg-title {:font-size   size/header-title
                    :font-weight "bold"
                    :color       color/header-title-font-shallow})
