(ns cpc-did.dev
  (:require [cpc-did.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
