(ns playground.nav.events
  (:require [re-frame.core :refer [reg-event-db]]))

;; (rf/dispatch [:set-active-nav :id-nav])
;; (fn [db [a b]]) -> (a <-> :set-active-nav) ^ (b <-> :id-nav)
(reg-event-db
 :set-active-nav
 (fn [db [_ active-nav]]
   (assoc-in db [:nav :active-nav] active-nav)))
