(ns cpc-did.utils.size-scale
  (:require ["react-native" :as rn]
            [cpc-did.utils.cljs-js-transit :as t]))

(def DEFAULT_WIDTH 375)
(def DEFAULT_HEIGHT 667)

(defn get-width
  [width]
  (-> ^js rn/Dimensions
      (.get "window")
      t/->cljs
      (#(-> (:width %)
            (/ DEFAULT_WIDTH)
            (* width)))))

(defn get-height
  [width]
  (-> ^js rn/Dimensions
      (.get "window")
      t/->cljs
      (#(-> (:height %)
            (/ DEFAULT_HEIGHT)
            (* width)))))

(defn get-window-size
  [size-type]
  (-> ^js rn/Dimensions
      (.get "window")
      t/->cljs
      (#(size-type %))))

(defn font-scale
  "Get font-scale, only for android"
  []
  (.getFontScale ^js rn/PixelRatio))

(defn pixel-ratio
  "Get pixel-ratio, used to pixel density"
  []
  (.get ^js rn/PixelRatio))


