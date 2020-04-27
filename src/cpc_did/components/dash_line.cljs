(ns cpc-did.components.dash-line
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            [cpc-did.styles.components.dash-line :as d-style]))

(defn dash-line
  "A simple dashed line component"
  [width len color]
  [:> rn/View {:style (merge d-style/dash-line
                             {:width width})}
   (for [n (range len)]
     (r/as-element
       [:> rn/View
        {:key (name (gensym (str "d" n)))
         :style (merge d-style/dash-item
                       {:background-color color})}]))])
