(ns cpc-did.utils.event-listener
  (:require ["react-native" :refer (DeviceEventEmitter)]))

(def LOADING "LOADING")
(def CLOSE "CLOSE")
(def SHOW_ERROR "SHOW_ERROR")
(def NETWORK_EXCEPTION "NETWORK_EXCEPTION")
(def HTTP_ERROR "FAIL")
(def HTTP_SUCCESS "SUCCESS")
(def LOADING_FINISH "LOADING_FINISH")

(defn add
  [name func]
  (.addListener ^js DeviceEventEmitter
                name
                func))
(defn once
  [name func]
  (.once ^js DeviceEventEmitter
                name
                func))

(defn emit
  [name param]
  (.emit ^js DeviceEventEmitter name param))


(defn remove-listener
  [name func]
  ;; func must be the same one with add
  ;; otherwise, the event will not be remove
  (.removeListener ^js DeviceEventEmitter name func))

(defn remove-all
  []
  (.removeAllListeners ^js DeviceEventEmitter))

(defn listeners
  [name]
  (.listeners ^js DeviceEventEmitter))
