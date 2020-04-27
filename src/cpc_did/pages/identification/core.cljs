(ns cpc-did.pages.identification.core
  (:require ["react-native" :as rn]
            ["react-native-shadow" :refer (BoxShadow)]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [cpc-did.utils.http :as http]
            [re-frame.core :refer [subscribe dispatch]]
            [cpc-did.components.gradient-line :as g-line]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.utils.react-navigation :refer [nav-reset]]
            [cpc-did.styles.identification-main :as i-styles]
            [cpc-did.utils.cljs-js-transit :as t]
            [cpc-did.utils.event-listener :as listener :refer [once add emit remove-listener]]))
;;; Actually, we should download the logo and show it

;;; An identification result list
(defn identification-list
  "An identification result list"
  [^js navigation title intro m-item]
  [:> BoxShadow {:setting i-styles/custom-options}
   [:> rn/View {:style i-styles/list-container}
    [:> rn/TouchableOpacity {:on-press (fn []
                                         (.navigate navigation "inst-intro" m-item))
                             :style i-styles/list-content}
     [list-item/three-item assets/attestation-vehicle title layout/list-title assets/arrow-enter]
     [:> rn/View {:style i-styles/detail-container}
      [:> rn/Text {:style i-styles/detail-text
                   :number-of-lines 2
                   :ellipsize-mode "tail"} intro]
      [:> rn/Image {:source assets/logo-GAC-vertical}]]]]])

(defn page
  [navigation state]
  (let [refreshing (r/atom false)
        fail (fn [err]
               (reset! refreshing false)
               (emit listener/SHOW_ERROR (:message err)))
        empty-height (r/atom 0)]
    (fn []
      [:> rn/View {:style layout/cross-axis-container}
       [:> rn/FlatList {:on-layout (fn [^js obj]
                                     (let [height (.. obj
                                                      -nativeEvent
                                                      -layout
                                                      -height)]
                                       (if (< @empty-height height)
                                         (reset! empty-height height))))
                        :data          @(subscribe [:claim-list])
                        :render-item (fn [^js item]
                                       (let [{:keys [type uri] :as m-item} (-> item
                                                                               .-item
                                                                               t/->cljs)]
                                         (r/as-element
                                           [identification-list navigation type uri m-item])))
                        :key-extractor (fn [_ index]
                                         (str index))
                        :refreshing    @refreshing
                        :onRefresh     (fn []
                                         (reset! refreshing true)
                                         ;;; only can listen once.
                                         ;;; because the event maybe conflict with identification info page
                                         (add listener/HTTP_ERROR fail)
                                         (once listener/HTTP_SUCCESS (fn []
                                                                       (reset! refreshing false)
                                                                       (remove-listener listener/HTTP_ERROR fail)))
                                         (http/request-wrapper http/CLAIM_INDEX_REFRESH nil))
                        :List-footer-component list-item/flat-list-footer
                        :ListEmptyComponent (list-item/flat-list-empty empty-height)
                        :shows-vertical-scroll-indicator false}]
       [g-line/gradient-line]])))

(defn show-toast
  [^js obj]
  (if (> @(subscribe [:cert-count]) 0)
    (.. obj -navigation (navigate "qr-code"))
    (emit listener/SHOW_ERROR "未认证用户")))
