(ns cpc-did.utils.date)

(defn prefix-integer
  [num length]
  (-> ^js (js/Array. length)
      (.join "0")
      (str num)
      (.slice (- 0 length))))

(defn to-time-string
  "docstring"
  [time]
  (let [date (js/Date. time)
        y (-> ^js date .getFullYear (prefix-integer 4))
        m (-> ^js date .getMonth (prefix-integer 2))
        d (-> ^js date .getDate (prefix-integer 2))
        h (-> ^js date .getHours (prefix-integer 2))
        mi (-> ^js date .getMinutes (prefix-integer 2))
        s (-> ^js date .getSeconds (prefix-integer 2))]
    (str y "-" m "-" d " " h ":" mi ":" s)))


