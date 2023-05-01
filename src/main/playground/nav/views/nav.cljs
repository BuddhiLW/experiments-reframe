(ns playground.nav.views.nav
  (:require
   [playground.nav.views.authenticated :refer [authenticated]]
   [playground.nav.views.public :refer [public]]))

(defn nav
  []
  (let [user false]
    (if user
      [authenticated]
      [public])))
