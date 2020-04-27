(ns cpc-did.styles.error-content
  (:require [cpc-did.styles.utils.size :as size]
            [cpc-did.styles.utils.color :as color]))

(def toast-text {:margin-top (size/v-margin 16)
                 :font-size size/header-title
                 :color color/font-white
                 :font-weight "bold"})
