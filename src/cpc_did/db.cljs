(ns cpc-did.db
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            ["react-native" :as rn]
            [cpc-did.utils.cljs-js-transit :as t]
            [cpc-did.utils.error :as err]
            [goog.object :as gobj]
            [cpc-did.utils.event-listener :refer [emit LOADING_FINISH]])
  (:require-macros [cpc-did.utils.macros :refer [promise->]]))

;;;; A namespace that includes spec, default-db,
;;;; local-storage reading and writing.

;;; -- Helper ---------------------------------------------
(defn ->cljs
  [data index]
  (-> data
      (aget index)
      t/json->cljs))

;;; --Default app-db Value --------------------------------
(def default-db
  {:keystore     ""                                         ;keystore. Use js/JSON.stringify transfer
   :username     ""
   :token        ""
   :cpc-id       ""
   :claim-detail nil
   :query-claim  nil
   :claim-list   (sorted-map)
   :cert-list    (vector)
   :auth-list    (vector)
   :is-register-did false})
   ;; to add for offline work
   ;:claim-detail-list (sorted-map)


;;; -- Local Storage --------------------------------------
(def items-key-list {:username "username"
                     :cpc-id "cpc-id"
                     :keystore "keystore"
                     :token "token"
                     :cert-list "cert-list"
                     :auth-list "auth-list"
                     :claim-list "claim-list"
                     :is-register-did "is-register-did"})

(defn item->storage
  "Store item to local storage, such as
  username, keystore, certification-list, authorization-list and so on
  Sometimes, we use a path as interceptor, then, we can only get an item,
  not the whole db"
  [m-key item? db _]
  (let [item (if item? db (db m-key))]
    (promise-> #(err/error {:code 604 :err %})
               (.setItem ^js rn/AsyncStorage
                         (items-key-list m-key)
                         (t/cljs->json item)))))

(defn storage->items
  "Store item to local storage, such as
  username, keystore, certification-list, authorization-list and so on"
  []
  (promise-> #(err/error {:code 603 :err %})
             (.multiGet  ^js rn/AsyncStorage
                         (t/->js (map #(% 1) items-key-list)))
             (fn [data]
               (let [items (atom {})]
                 (gobj/map data (fn [entry]
                                  (if-not (nil? (aget entry 1))
                                    (swap! items #(assoc %
                                                    (keyword (aget entry 0))
                                                    (t/json->cljs (aget entry 1)))))))
                 (dispatch-sync [:initialise-db @items])
                 (if (:keystore @items)
                   "app-stack"
                   "start-stack")))))

(defn vc->storage
  "docstring"
  [vc]
  (promise-> #(err/error {:code 603 :err %})
             (.getItem ^js rn/AsyncStorage "vc-list")
             #(t/json->cljs %)
             #(let [data %]
                (if data
                  (conj data vc)
                  (conj (vector) vc)))
             #(t/cljs->json %)
             (fn [data]
               (.setItem ^js rn/AsyncStorage "vc-list" data))))

;;; return a promise
(defn get-vc
  [index]
  (promise-> #(err/error {:code 603 :err %})
             (.getItem ^js rn/AsyncStorage "vc-list")
             #(t/json->cljs %)
             #(% index)))
