(ns playground.recipes.events
  (:require
   [re-frame.core :as rf]))

(rf/reg-event-db
 :recipes/save-recipe
 (fn [db [_ recipe-id]]
   (let [uid (get-in db [:auth :uid])]
     (-> db
         (update-in [:users uid :saved] conj recipe-id)
         (update-in [:recipes recipe-id :saved-count] inc)))))

(rf/reg-event-db
 :recipes/unsave-recipe
 (fn [db [_ recipe-id]]
   (let [uid (get-in db [:auth :uid])]
     (-> db
         (update-in [:users uid :saved] disj recipe-id)
         (update-in [:recipes recipe-id :saved-count] dec)))))

(rf/reg-event-db
 :recipes/open-modal
 (fn [db [_ modal-name]]
   (assoc-in db [:nav :active-modal] modal-name)))
