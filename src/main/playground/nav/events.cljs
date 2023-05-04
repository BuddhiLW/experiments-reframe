(ns playground.nav.events
  (:require
   [playground.router :as router]
   [re-frame.core :refer [reg-event-db
                          reg-fx]]))

(reg-fx
 :navigate-to
 (fn [{:keys [path]}]
   (router/set-token! path)))

(reg-event-db
 :route-changed
 (fn [db [_ {:keys [handler]}]]
   (assoc-in db [:nav :active-page] handler)))

(reg-event-db
 :set-active-nav
 (fn [db [_ active-nav]]
   (assoc-in db [:nav :active-nav] active-nav)))

(comment)
 ;; (rf/dispatch [:set-active-nav :id-nav]))
 ;; (fn [db [a b]]) -> (a <-> :set-active-nav) ^ (b <-> :id-nav))
