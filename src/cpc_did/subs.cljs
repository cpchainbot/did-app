(ns cpc-did.subs
  (:require [re-frame.core :refer [reg-sub path]]))

;;;; Define some function to handler app-state change.
;;;; These method handler will use for subscribing.

(reg-sub
  :username
  (fn [db _]
    (:username db)))

(reg-sub
  :cpc-id
  (fn [db _]
    (:cpc-id db)))

(reg-sub
  :keystore
  (fn [db _]
    (:keystore db)))

(reg-sub
  :token
  (fn [db _]
    (:token db)))

(reg-sub
  :claim-list
  (fn [db _]
    (-> db :claim-list :list)))

(reg-sub
  :cert-list
  (fn [db _]
    (:cert-list db)))

(reg-sub
  :auth-list
  (fn [db _]
    (:auth-list db)))

(reg-sub
  :cert
  (fn [db [_ index]]
    (nth (:cert-list db) index)))

(reg-sub
  :auth
  (fn [db [_ index]]
    (nth (:auth-list db) index)))

(reg-sub
  :claim-detail
  (fn [db _]
    (:claim-detail db)))

(reg-sub
  :query-claim
  (fn [db [_ index]]
    (nth (-> db :claim-list :list) index)))

(reg-sub
  :cert-count
  (fn [db _]
    (count (:cert-list db))))

(reg-sub
  :auth-count
  (fn [db _]
    (count (:auth-list db))))

(reg-sub
  :is-register-did
  (fn [db _]
    (:is-register-did db)))
