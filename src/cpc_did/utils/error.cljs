(ns cpc-did.utils.error
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            [cpc-did.utils.event-listener :as listener :refer [emit]]
            [cpc-did.styles.utils.layout :as layout]
            [cpc-did.components.static-assets :as assets]
            [cpc-did.styles.error-content :as e-content]))
;;;-- error content ----------------------------------------
(defn decrypt-error-content
  [err]
  [:> rn/View {:style layout/container-center}
   [:> rn/Image {:source assets/toast-error}]
   [:> rn/Text {:style e-content/toast-text} err]])

;;; -- error type ------------------------------------------
(defn decrypt-error
  "docstring"
  [err]
  (if (string? err)
    (emit listener/SHOW_ERROR (r/as-element [decrypt-error-content "keystore错误"]))
    (emit listener/SHOW_ERROR (r/as-element [decrypt-error-content "密码错误"]))))

(defn encrypt-error
  "docstring"
  [err]
  (emit listener/SHOW_ERROR "生成keystore出错"))

(defn read-storage-error
  "docstring"
  [err]
  (emit listener/SHOW_ERROR "内存读取出错"))

(defn write-storage-error
  "docstring"
  [err]
  (emit listener/SHOW_ERROR "内存写入出错"))

(defn network-error
  [err]
  (emit listener/SHOW_ERROR "网络异常"))

;;; judge error type by error code
;;; 1xx-5xx http status code
;;; 6xx     custom code
;;;
;;; custom code:
;;; 601 decrypt error
;;; 602 encrypt error
;;; 603 read storage error
;;; 604 write storage error
;;; 605 bad network
;;;
;;; http status code
;;; 401 unauthorized, which means failed to register did
;;; 403 forbidden, which means failed to get token
;;; 408 request timeout
;;;
;;; server code
;;; include some server error code, need to add.
;;; 701 register credential failed
;;; 702 verify credential failed
;;; 703 invalid token
(defn error
  [{:keys [code err] :as data}]
  (println data)
  (case code
    601 (decrypt-error err)
    602 (encrypt-error err)
    603 (read-storage-error err)
    604 (write-storage-error err)
    605 (network-error err)
    401 (emit listener/HTTP_ERROR {:code 401 :message "注册DID失败"})
    403 (emit listener/HTTP_ERROR {:code 403 :message "获取token失败"})
    408 (emit listener/HTTP_ERROR {:code 408 :message "请求超时"})
    701 (emit listener/HTTP_ERROR {:code 701 :message "注册证书失败"})
    702 (emit listener/HTTP_ERROR {:code 702 :message "验证证书失败"})
    703 (emit listener/HTTP_ERROR {:code 703 :message "token失效"})))

