(ns cpc-did.components.gradient-background
  (:require ["expo-linear-gradient" :as lg]
            [cpc-did.styles.utils.color :as color]))

(defn background
  "docstring"
  [style view]
  [:> lg/LinearGradient {:style  style
                         :colors color/linear-colors
                         :start  #js [0 0]
                         :end    #js[0 1]}
   view])
