(ns playground.nav.views.nav
  (:require
   [playground.nav.views.authenticated :refer [authenticated]]
   [playground.nav.views.public :refer [public]]
   [re-frame.core :as rf]))

(defn nav
  []
  (let [logged-in? @(rf/subscribe [:logged-in?])]
    (if logged-in?
      [authenticated]
      [public])))
