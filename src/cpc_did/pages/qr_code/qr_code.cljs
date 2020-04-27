(ns cpc-did.pages.qr-code.qr-code
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            [cpc-did.utils.cljs-js-transit :as t]
            [cpc-did.components.camera :as camera]
            [cpc-did.utils.event-listener :as listener :refer [emit]]
            [cpc-did.utils.validates :as validate])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn page
  [^js navigation state]
  (let [back-func #(.pop navigation)
        scanned-func (fn [^js obj]
                       (if (.isFocused navigation)
                         (try
                           (let [data (-> obj
                                          .-data
                                          ((fn [s]
                                             (let [pat (js/RegExp "(^\\s*)|(\\s*$)" "g")
                                                   std-str (.replace ^js s pat "")]
                                               (.parse js/JSON std-str))))
                                          t/->cljs)]
                             (if (and (validate/cpc-did? (:verifier data))
                                      (validate/location? (:location data)))
                               (.navigate navigation "authorize" data)
                               (emit listener/SHOW_ERROR "二维码无效，请扫描其他二维码")))
                           (catch :default e (emit listener/SHOW_ERROR "二维码无效，请扫描其他二维码")))))]
    [camera/scanner "扫码验证" "请将二维码或条形码放入框内" back-func scanned-func]))

