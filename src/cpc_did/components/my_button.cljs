(ns cpc-did.components.my-button
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            [cpc-did.styles.components.button :as b-styles]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.components.static-assets :as assets]))

(defn button
  "Custom button"
  [on-press title button-style state]
  [:> rn/TouchableOpacity {:style button-style
                           :on-press on-press}
   [:> rn/View
    [:> rn/Text {:style (b-styles/title state)} title]]])

(defn start-page-button
  "For the start pages button"
  [is-selected title on-press]
  [button
   on-press
   title
   (b-styles/start-page-button is-selected)
   (if is-selected
     "selected"
     "normal")])

(defn normal-page-button
  [is-touchable title  on-press]
  [button
   (if is-touchable
     on-press
     nil)
   title
   b-styles/normal-page-button
   (if is-touchable
     "normal"
     "untouchable")])

(defn small-page-button
  "docstring"
  [is-touchable title on-press]
  [button
   (if is-touchable
     on-press
     nil)
   title
   b-styles/small-page-button
   (if is-touchable
     "normal"
     "untouchable")])

(defn hint-button
  [margin-top text on-press]
  [:> rn/View {:style b-styles/hint-button-container}
   [:> rn/TouchableOpacity {:style (b-styles/hint-button-content margin-top)
                            :on-press on-press}
    [:> rn/Image {:source assets/question}]
    [:> rn/Text {:style b-styles/hint} text]]])

(defn text-button
  [title on-press margin-top]
  [:> rn/TouchableOpacity {:style (b-styles/text-button-title margin-top)
                           :on-press on-press}
   [:> rn/Text {:style (b-styles/title "selected")} title]])
