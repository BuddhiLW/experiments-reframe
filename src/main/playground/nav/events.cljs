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
 (fn [db [_ {:keys [route-params handler]}]]
   #_(js/console.log "route-changed" route)
   (-> db
       (assoc-in [:nav :active-page] handler)
       (assoc-in [:nav :active-recipe] (keyword (:recipe-id route-params))))))

(reg-event-db
 :set-active-nav
 (fn [db [_ active-nav]]
   (assoc-in db [:nav :active-nav] active-nav)))

(reg-event-db
 :set-active-page
 (fn [db [_ active-page]]
   (assoc-in db [:nav :active-page] active-page)))

(reg-event-db
 :recipes/close-modal
 (fn [db _]
   (assoc-in db [:nav :active-modal]  nil)))

(comment)
 ;; (rf/dispatch [:set-active-nav :id-nav]))
 ;; (fn [db [a b]]) -> (a <-> :set-active-nav) ^ (b <-> :id-nav))
