(ns cpc-did.utils.validates
  (:require ["validid" :as validate-id]
            [cpc-did.utils.cljs-js-transit :as t]))

(defn username?
  "Don't afford rarely used word.
  Can reference https://www.jianshu.com/p/1438e5d03313"
  [s]
  (let [v #"^[a-zA-Z0-9\u4e00-\u9fa5]+$"]
    (.test v s)))

(defn person-name?
  "Don't afford rarely used word.
  Can reference https://www.jianshu.com/p/1438e5d03313"
  [s]
  (let [v #"^[\u4e00-\u9fa5]+$"]
    (.test v s)))

(defn password?
  [p]
  (let [v #"(?=.*[a-z])(?=.*[A-Z])(?=.*\d)^\S{6,16}$"]
    (.test v p)))
(defn id-card-format?
  [id]
  (let [v #"^[0-9a-zA-Z]+$"]
    (.test v id)))
(defn id-card?
  [id]
  (and (id-card-format? id)
       (or (.cnid validate-id id)
           (.twid validate-id id)
           (.hkid validate-id id))))

;;; ---- check vin-------------------------------------------
(defn vin-form?
  [vin]
  (let [v "[a-hj-npr-z0-9]"
        reg (js/RegExp. (str "^" v "{8}[0-9xX]" v "{8}$") "i")]
    (.test reg vin)))

(defn to-upper
  [^js vin]
  (.toUpperCase vin))

(defn split-vin
  [^js vin]
  (.match vin (js/RegExp. "\\d|[A-Z]" "g")))

(def letter-map {"A" 1 "B" 2 "C" 3 "D" 4 "E" 5
                 "F" 6 "G" 7 "H" 8 "J" 1 "K" 2
                 "L" 3 "M" 4 "N" 5 "P" 7 "R" 9
                 "S" 2 "T" 3 "U" 4 "V" 5 "W" 6
                 "X" 7 "Y" 8 "Z" 9 "1" 1 "2" 2
                 "3" 3 "4" 4 "5" 5 "6" 6 "7" 7
                 "8" 8 "9" 9 "0" 0})
(def vin-weights [8 7 6 5 4 3 2 10 0 9 8 7 6 5 4 3 2])
(defn vin-check-list
  [vin]
  (map (fn [c1 c2] (* (letter-map c1) c2)) vin vin-weights))

(defn sum
  [coll]
  (reduce + coll))

(defn check-digit
  "Compute a check digit for vin"
  [vin]
  (let [get-check-sum (comp sum vin-check-list t/->cljs split-vin to-upper)
        check-sum (get-check-sum vin)]
    (if (-> check-sum (mod 11) (= 10))
      "X"
      (-> check-sum (mod 11) str))))

(defn check-digit?
  [vin]
  (= (check-digit vin)
     (-> vin to-upper split-vin (get 8))))

;;; 5J6HYJ8V55L009357
(defn vin?
  [vin]
  (and (vin-form? vin)
       (check-digit? vin)))
;;;-------------------------------check cpc-id and location ---------------------------------------

(defn cpc-did?
  "A simple regexp to check cpc-did,
  just validate format."
  [did]
  (let [v #"^did:cpc:[0-9a-z]{40}$"]
    (.test v did)))
(defn location?
  "A simple regexp to check location,
  just validate format"
  [loc]
  (let [v #"^[\u4e00-\u9fa5]+[0-9a-zA-Z\u4e00-\u9fa5 ]+$"]
    (.test v loc)))
