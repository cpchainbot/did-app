(ns cpc-did.pages.user.core
  (:require ["react-native" :as rn]
            ["expo-linear-gradient" :as lg]
            ["ethers" :refer (ethers)]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [cpc-did.components.gradient-line :as g-line]
            [cpc-did.components.list-item :as list-item]
            [cpc-did.styles.user-main :as u-main]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.utils.color :as color]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.bottom-modal :as b-modal])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn m-list-item
  [l-icon title has_border]
  [:> rn/View {:style (u-main/m-list-item has_border)}
   [:> rn/View {:style u-main/list-container}
    [list-item/three-item l-icon title u-main/list-tile assets/arrow-enter]]])

(defn m-list-item-touchable
  [l-icon title on-press has_border]
  [:> rn/TouchableOpacity {:style u-main/m-list-item-touchable
                           :on-press on-press}
   [m-list-item l-icon title has_border]])

(defn page
  "A use core interface"
  [navigation state]
  (let [modal-visible (r/atom false)
        set-modal-visible #(swap! modal-visible not)
        bottom-visible (r/atom false)
        set-bottom-visible #(swap! bottom-visible not)
        nav (fn [val]
              (.navigate navigation "user-backup"))]
    (fn []
      [:> rn/View {:style u-main/container}
       [:> lg/LinearGradient {:style u-main/header-info-container
                              :colors color/linear-colors
                              :start #js [0 0]
                              :end #js[0 1]}
        [:> rn/View {:style u-main/header-container}
         [:> rn/Text {:style u-main/title} "我的"]
         [:> rn/Image {:style u-main/more-func
                       :source assets/more}]]
        [:> rn/View {:style u-main/user-info}
         [:> rn/View {:style u-main/avatar-info}
          [:> rn/Image {:source assets/avatar}]
          [:> rn/Text {:style u-main/user-name} @(subscribe [:username])]]
         [:> rn/View {:style u-main/user-info-text}
          [:> rn/Text {:style u-main/user-cpc} "CPC ID:"]
          [:> rn/Text {:style u-main/user-cpc-content} @(subscribe [:cpc-id])]]]]
       [:> rn/View
        [:> rn/View {:style u-main/first-group}
         [m-list-item-touchable assets/credential-record "我的认证"  #(.navigate navigation "cert-records") true]
         [m-list-item-touchable assets/authorization_record "验证记录" #(.navigate navigation "auth-records") false]]
        [:> rn/View {:style u-main/second-group}
         [m-list-item-touchable assets/keystore-backup "备份keystore" (fn []
                                                                      (set-modal-visible)
                                                                      (set-bottom-visible)) false]]]
       [:> rn/Modal {:animationType "fade"
                     :transparent   true
                     :visible       @modal-visible}
        (if @bottom-visible
          [b-modal/bottom-modal set-modal-visible set-bottom-visible nav])]
       [g-line/gradient-line]])))
