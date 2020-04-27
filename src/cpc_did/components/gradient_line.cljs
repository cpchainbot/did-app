(ns cpc-did.components.gradient-line
  (:require ["expo-linear-gradient" :as lg]
            [reagent.core :as r]
            [cpc-did.styles.utils.size :as size]))

(defn gradient-line
  []
  [:> lg/LinearGradient {:style {:flex 0
                                 :height 1
                                 :width "100%"
                                 :position "absolute"
                                 :bottom 0}
                         :locations #js [0.1 0.4 0.6 0.9]
                         :colors #js ["rgba(222, 222, 222, 0.1)" "rgba(244, 244, 244, 1)" "rgba(244, 244, 244, 1)" "rgba(222, 222, 222, 0.1)"]
                         :start #js [0 0]
                         :end #js [1 0]}])
(defn gradient-line-qr
  []
  [:> lg/LinearGradient {:style {:flex 0
                                 :height size/h-4
                                 :width size/w-168}
                         :locations #js [0 0.5 1]
                         :colors #js ["rgba(70, 100, 226, 0)" "rgba(70, 100, 226, 1)" "rgba(70, 100, 226, 0)"]
                         :start #js [0 0]
                         :end #js [1 0]}])