(ns cpc-did.pages.help.vin-help
  (:require ["react-native" :as rn]
            [cpc-did.styles.utils.layout :as layout]))

(defn page
  "docstring"
  [^js navigation state]
  [:> rn/View {:style layout/cross-axis-container}
   [:> rn/Text
    {:style layout/help-text}
    "VIN码是表明车辆身份的代码，由17位字符（包括英文字母和数字）组成，是制造厂为了识别车辆而指定的\"汽车身份证\"。"]])
