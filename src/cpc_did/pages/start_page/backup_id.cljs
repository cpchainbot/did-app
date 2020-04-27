(ns cpc-did.pages.start-page.backup-id
  (:require ["react-native" :as rn :refer (Clipboard)]
            ["react-native-easy-toast" :default Toast :refer (DURATION)]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [cpc-did.utils.event-listener :refer [emit SHOW_ERROR]]
            [cpc-did.styles.backup-id :as b-style]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.my-button :as m-button]))

(defn alert-modal
  [visible set-visible]
  [:> rn/Modal {:visible @visible
                :transparent true
                :animationType "fade"}
   [:> rn/View {:style layout/transparent-modal-background}
    [:> rn/View {:style b-style/modal-container}
     [:> rn/Image {:source assets/disable-screen-snapshot}]
     [:> rn/Text {:style b-style/modal-title} "禁止截屏"]
     [:> rn/Text {:style b-style/modal-content} "不要将Keystore备份信息截图或拍照储存！并确保您的周围没有其他人或摄像头。"]
     [:> rn/View {:style b-style/modal-button-container}
      [m-button/small-page-button true "好的" set-visible]]]]])

(defn page
  "docstring"
  [navigation state]
  (let [keystore (.parse js/JSON @(subscribe [:keystore]))
        toast-ref (r/atom nil)
        visible (r/atom false)
        set-visible #(reset! visible (not @visible))]
    (r/create-class
      {:component-did-mount (fn [this]
                              (set-visible))
       :reagent-render (fn []
                         [:> rn/View {:style layout/cross-axis-container}
                          [:> rn/View {:style layout/flex-0}
                           [:> rn/Text {:style b-style/hint} "请将keystore文件复制粘贴到安全、离线的地方妥善保存。"]
                           [:> rn/View {:style layout/keystore-container}
                            [:> rn/ScrollView {:shows-vertical-scroll-indicator false}
                             [:> rn/TextInput {:style     b-style/keystore
                                               :multiline true
                                               :editable  false}
                              keystore]]]
                           [m-button/hint-button 24 "我该如何使用它？" nil]
                           [:> rn/View {:style b-style/button-container}
                            [m-button/normal-page-button true "复制Keystore" (fn []
                                                                             (.setString ^js Clipboard keystore)
                                                                             (emit SHOW_ERROR "复制成功"))]]
                           [alert-modal visible set-visible]]])})))
