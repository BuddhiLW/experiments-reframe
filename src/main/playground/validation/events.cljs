(ns playground.validation.events
  (:require
   [playground.spec :refer [check-spec-interceptor]]
   [re-frame.core :as rf]))

(def validation-interceptor [check-spec-interceptor])

(rf/reg-event-db
 :form/has-value?
 validation-interceptor
 (fn [db [_ id]]
   (assoc-in db [:errors id] "Can't be blank")))

(rf/reg-event-db
 :form/clear-error
 validation-interceptor
 (fn [db [_ id]]
   (update-in db [:errors] dissoc id)))
