(ns playground.auth.views.profile
  (:require [playground.components.page-nav :refer [page-nav]]))

(defn profile
  []
  [page-nav {:center "Profile"}])
