(ns playground.become-a-chef.events
  (:require
   [playground.spec :refer [check-spec-interceptor]]
   [re-frame.core :as rf]))

(rf/reg-event-fx
 :become-a-chef/agree
 [check-spec-interceptor]
 (fn [{:keys [db]} _]
   (let [user (get-in db [:auth :uid])]
     {:db (assoc-in db [:users user :role] :chef)
      :dispatch [:recipes/close-modal]})))
