(ns playground.auth.views.log-in
  (:require [playground.components.page-nav :refer [page-nav]]))

(defn log-in
  []
  [page-nav {:center "Log in"}])
