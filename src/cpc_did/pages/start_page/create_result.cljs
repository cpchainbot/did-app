(ns cpc-did.pages.start-page.create-result
  (:require ["react-native" :as rn :refer (Keyboard)]
            ["ethers" :refer (ethers)]
            ["react-native-easy-toast" :default Toast :refer (DURATION)]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [cpc-did.utils.react-navigation :refer [nav-reset]]
            [cpc-did.styles.create-result :as c-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.my-button :as m-button]
            [cpc-did.components.bottom-modal :as b-modal]))

;;; modal

(defn page
  "docstring"
  [^js navigation state]
  (let [modal-visible (r/atom false)
        set-modal-visible #(swap! modal-visible not)
        bottom-visible (r/atom false)
        set-bottom-visible #(swap! bottom-visible not)
        nav (fn [val]
              (nav-reset navigation "start-backup"))]
    (fn []
      [:> rn/View {:style layout/cross-axis-container}
       [:> rn/Image {:style  c-style/avatar
                     :source assets/avatar-res}]
       [:> rn/Text {:style c-style/username} @(subscribe [:username])]
       [:> rn/View {:style c-style/id-container}
        [:> rn/Text {:style c-style/id-title} "您的CPC ID:"]
        [:> rn/Text {:style c-style/id-content} @(subscribe [:cpc-id])]]
       [:> rn/Text {:style c-style/intro-title} "什么是CPC ID？"]
       [:> rn/Text {:style c-style/intro-detail} "CPC ID是区块链世界的通行证，基于去中心化身份标识协议，实现现实世界中的人、财、物、事在链上的标识、认证和通证。"]
       [:> rn/View {:style c-style/backup-container}
        [m-button/normal-page-button true "备份" (fn []
                                                 (set-modal-visible)
                                                 (set-bottom-visible))]]
       [m-button/text-button "开始，稍后备份" #(.navigate navigation "app-stack") 24]
       [:> rn/Modal {:animationType "fade"
                     :transparent   true
                     :visible       @modal-visible}
        (if @bottom-visible
          [b-modal/bottom-modal set-modal-visible set-bottom-visible nav])]])))
