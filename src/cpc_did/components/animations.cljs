(ns cpc-did.components.animations
  (:require ["lottie-react-native" :as LottieView]
            ["react-native" :as rn]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.components.animations :as a-style]))

;;;-- helper function -----------------------------------
(defn  after-animation
  "Use runAfterAnimation to switch between animation"
  [func]
  (.runAfterInteractions ^js rn/InteractionManager func))

(defn delay-job
  [func time]
  (js/setTimeout
    #(after-animation func)
    time))

;;;-- animation------------------------------------------
(defn loading
  "docstring"
  []
  [:> LottieView
   {:source assets/ani-loading
    :autoPlay true
    :loop true
    :style a-style/loading}])

(defn toast-loading
  []
  [:> rn/View {:style a-style/toast-loading-container}
   [:> LottieView
    {:source assets/toast-loading
     :autoPlay true
     :loop true
     :style a-style/toast-loading}]])
