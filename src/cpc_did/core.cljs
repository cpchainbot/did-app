(ns cpc-did.core
  (:require ["react-native" :as rn :refer (Platform)]
            ["react-native-screens" :refer (useScreens)]
            ["react-native-easy-toast" :default Toast :refer (DURATION)]
            [reagent.core :as r]
            [cpc-did.router :as router]
            [cpc-did.styles.core :as core]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.db :refer [item->storage]]
            [cpc-did.utils.register-app :as register]
            [cpc-did.utils.event-listener :as listener :refer [add remove-all]]))
;;; clear storage
;(.clear ^js rn/AsyncStorage)
;;; will generate a immediate exec event.

(defn app
  []
  (let [network-visible (r/atom false)
        set-network-visible #(reset! network-visible (not @network-visible))
        toast-ref (atom nil)]

    (r/create-class
      {:component-did-mount (fn [this]
                              ;(add
                              ;  listener/NETWORK_EXCEPTION
                              ;  (fn []
                              ;    (set-network-visible)
                              ;    (js/setTimeout set-network-visible 1000)))
                              (add
                                listener/SHOW_ERROR
                                #(.show @toast-ref % 500))
                              (add
                                listener/LOADING
                                #(.show @toast-ref % (.-FOREVER ^js DURATION)))
                              (add
                                listener/CLOSE
                                #(.close @toast-ref)))
       :reagent-render (fn []
                         [:> rn/View {:style core/container}
                          ;(if @network-visible
                          ;  [:> rn/View {:style core/network-error-container}
                          ;   [:> rn/Text {:style core/network-error} "网络异常"]])
                          [:> router/app-container]
                          [:> Toast (merge {:ref      #(reset! toast-ref %)
                                            :position "center"
                                            :position-value 0}
                                           layout/toast)]])
       :component-will-unmount (fn [this]
                                 (remove-all))})))

(defn start
  {:dev/after-load true}
  []
  ;(if (= (.-OS Platform) "ios")
  ;  (useScreens))
  (register/render-root
    (r/as-element [app])))

(defn init []
  (start))
