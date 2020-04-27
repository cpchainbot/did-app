(ns cpc-did.pages.start-page.import-username
  (:require ["react-native" :as rn]
            ["react-native-keyboard-aware-scroll-view" :refer (KeyboardAwareScrollView)]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [cpc-did.utils.validates :as validate]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.styles.import-username :as i-style]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.components.form-item :as form-item]
            [cpc-did.components.my-button :as m-button]))

(defn page
  "docstring"
  [^js navigation state]
  (let [value (r/atom {})
        get-val (fn [key val]
                  (swap! value  assoc key  val))]
    (fn []
      [:> rn/View {:style layout/cross-axis-container}
       [:> KeyboardAwareScrollView
        (merge layout/keyboard-scroll-options i-style/extra-height)
        [:> rn/Image {:style  i-style/avatar
                      :source assets/avatar-res}]
        [form-item/normal-item
         "用户昵称"
         "支持汉字/数字/字母"
         validate/username?
         get-val
         i-style/form-item
         "username"]
        [:> rn/View {:style i-style/button-container}
         [m-button/normal-page-button
          (if (:username @value) true false)
          "确定"
          (fn []
            (dispatch [:add-username (:username @value)])
            (.navigate navigation "app-stack"))]]]])))


