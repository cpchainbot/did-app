(ns cpc-did.pages.identification.vin-scanner
  (:require [reagent.core :as r]
            [cpc-did.components.camera :as camera]
            [cpc-did.utils.event-listener :as listener :refer [emit]]
            [cpc-did.utils.validates :as validate])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

(defn page
  [^js navigation state]
  (let [func (:params state)
        back-func #(.pop navigation)
        scanned-func (fn [^js obj]
                       ;(.navigate navigation "authorize")
                       (if (.isFocused navigation)
                         (do
                           (try
                             (let [data (-> obj
                                            .-data
                                            ((fn [s]
                                               (let [pat (js/RegExp "(^\\s*)|(\\s*$)" "g")
                                                     std-str (.replace ^js s pat "")]
                                                 std-str))))]
                               (if (validate/vin? data)
                                 (do
                                   (.goBack navigation)
                                   (func data))
                                 (emit listener/SHOW_ERROR "VIN码无效，请扫描其他VIN码")))
                             (catch :default e (emit listener/SHOW_ERROR "VIN码无效，请扫描其他VIN码"))))
                         nil))]
    [camera/scanner "扫描VIN" "请将二维码或条形码放入框内" back-func scanned-func]))
