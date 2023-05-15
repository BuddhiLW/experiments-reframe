(ns playground.validation.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :form/errors
 (fn [db _]
   (:errors db)))
