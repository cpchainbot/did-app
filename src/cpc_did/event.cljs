(ns cpc-did.event
  (:require [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx after path]]
            [cpc-did.db :refer [storage->items item->storage default-db]]))

;;;; Define some interceptors to do others works
;;;; before and after handlers.
;;;; Define some functions to handler events, which will change app-state, aka "state of world".
;;;; These method handler will use for dispatch.

;;; -- Interceptor -----------------------------------------
;; When use a `path`, we only get the item, rather than the whole db
(defn ->storage
  [m-key item?]
  (after (partial item->storage m-key item?)))
;;; -- Handler ---------------------------------------------
(reg-event-fx
  :initialise
  (fn [_ _]
    {:db default-db}))

(reg-event-db
  :initialise-db
  (fn [db [_ data]]
    (merge db data)))

(reg-event-db
  :add-username
  [(->storage :username false)]
  (fn [db [_ username]]
    (assoc db :username username)))

(reg-event-db
  :add-keystore
  [(->storage :keystore false)]
  (fn [db [_ keystore]]
    (assoc db :keystore keystore)))


(reg-event-db
  :add-cpc-id
  [(->storage :cpc-id false)]
  (fn [db [_ cpc-id]]
    (assoc db :cpc-id cpc-id)))

(reg-event-db
  :add-token
  [(->storage :token false)]
  (fn [db [_ token]]
    (assoc db :token token)))

(reg-event-db
  :add-cert
  [(path :cert-list)
   (->storage :cert-list true)]
  (fn [cert-list [_ cert]]
    (conj cert-list cert)))

(reg-event-db
  :add-auth
  [(path :auth-list)
   (->storage :auth-list true)]
  (fn [auth-list [_ auth]]
    (conj auth-list auth)))

(reg-event-db
  :add-claim-list
  [(->storage :claim-list false)]
  (fn [db [_ claim-list issuer]]
    (assoc db :claim-list {:list   claim-list
                           :issuer issuer})))
(reg-event-db
  :add-claim-detail
  (fn [db [_ claim]]
    (assoc db :claim-detail claim)))

(reg-event-db
  :is-register-did
  [(->storage :is-register-did false)]
  (fn [db [_ val]]
    (assoc db :is-register-did val)))
