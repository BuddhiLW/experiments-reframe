(ns playground.validation.events
  (:require
   [re-frame.core :as rf]))

(rf/reg-event-db
 :form/has-value?
 (fn [db [_ id]]
   (assoc-in db [:errors id] "Can't be blank")))

(rf/reg-event-db
 :form/clear-error
 (fn [db [_ id]]
   (update-in db [:errors] dissoc id)))
