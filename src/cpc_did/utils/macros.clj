(ns cpc-did.utils.macros)

;;; A simple macro for Promise
(defmacro promise->
  "(-> ~promise ~@(map (fn [expr] (list '.then expr)) body)
  will expand to as
  (-> promise
      (.then body-first-entry)
      (.then body-second-entry)
      ...)
  So, please confirm you sequence of the function evaluation.
  .then will return a promise
  "
  [err-fn promise & body]
  `(.catch
     (-> ~promise
         ~@(map (fn [expr] (list '.then expr)) body))
     (fn [error#]
       (~err-fn error#))))