(ns playground.utilites
  (:require
   ["@mui/material" :refer [colors]]))

(defn color
  "Specify the vector of color and intensity (keys)"
  [[color intensity]]
  (get-in (js->clj colors :keywordize-keys true) [color intensity]))
