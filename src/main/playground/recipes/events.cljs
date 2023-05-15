(ns playground.recipes.events
  (:require
   [ajax.core :as ajax]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))

(def recipes-endpoint "https://gist.githubusercontent.com/jacekschae/50ffe6e8851a5dfe35e932682ca32d85/raw/06e8041d0abf86e2c5d809a334cf8f18d3d6303b/recipes.json")

(rf/reg-event-fx
 :http/get-recipes
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:loading :recipes] true)
    :http-xhrio {:method          :get
                 :uri             recipes-endpoint
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:recipes/set-recipes]
                 :on-failure      [:error/endpont-request :get-recipes]}}))

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

(rf/reg-event-fx
 :recipe/save-ingredient
 (fn [{:keys [db]} [_ {:keys [id name amount measure]}]]
   (let [active-recipe (get-in db [:nav :active-recipe])
         ingredients   (get-in db [:recipes active-recipe :ingredients])
         order (or (get-in ingredients [id :order])
                   (inc (count ingredients)))]
     {:db (assoc-in db [:recipes active-recipe :ingredients id]  {:id id
                                                                  :name name
                                                                  :amount amount
                                                                  :measure measure
                                                                  :order order})
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/delete-ingredient
 (fn [{:keys [db]} [_ ingredient-id]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (update-in db [:recipes active-recipe :ingredients] dissoc ingredient-id)
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/save-step
 (fn [{:keys [db]} [_ {:keys [id desc]}]]
   (let [active-recipe (get-in db [:nav :active-recipe])
         steps         (get-in db [:recipes active-recipe :steps])
         new-order     (if (number? (get-in steps [id :order]))
                         (get-in steps [id :order])
                         (inc (count steps)))
         data (js/console.log "data" {:id id
                                      :order new-order
                                      :desc desc})]

     {:db (assoc-in db [:recipes active-recipe :steps id]  {:id id
                                                            :order new-order
                                                            :desc desc})
      :dispatch-n [#_[:steps/reorder-steps]
                   [:recipes/close-modal]]})))

(rf/reg-event-db
 :steps/reorder-steps
 (fn [db _]
   (let [active-recipe (get-in db [:nav :active-recipe])
         steps         (get-in db [:recipes active-recipe :steps])
         sorted-steps  (->> steps
                            (map (fn [[k v]] [k (get v :order)]))
                            (sort-by second)
                            (map first))]
     (-> db
         (update-in [:recipes active-recipe :steps] (fn [steps]
                                                      (zipmap sorted-steps steps)))))))

(rf/reg-event-fx
 :recipe/delete-step
 (fn [{:keys [db]} [_ step-id]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (update-in db [:recipes active-recipe :steps] dissoc step-id)
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/save-recipe
 (fn [{:keys [db]} [_ {:keys [name prep-time]}]]
   (let [active-recipe (get-in db [:nav :active-recipe])
         id (or active-recipe (keyword (str "recipe-" (random-uuid))))
         uid (get-in db [:auth :uid])]
     {:db (update-in db [:recipes id] merge {:id id
                                             :name name
                                             :prep-time (js/parseInt prep-time)
                                             :cook uid
                                             :public? false})
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/delete-recipe
 (fn [{:keys [db]} _]
   (let [recipe (get-in db [:nav :active-recipe])]
         ;; uid (get-in db [:auth :uid])]
     {:db (update db :recipes  dissoc recipe)
      :dispatch-n [[:set-active-page :recipes]
                   [:set-active-nav :recipes]]
      :navigate-to {:path "/recipes/"}})))

(rf/reg-event-fx
 :recipe/publish
 (fn [{:keys [db]} [_ {:keys [price]}]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (update-in db [:recipes active-recipe] merge {:price (js/parseInt price)
                                                        :public? true})
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/unpublish
 (fn [{:keys [db]} _]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (assoc-in db [:recipes active-recipe :public?] false)
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/upsert-image
 (fn [{:keys [db]} [_ {:keys [img]}]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (assoc-in db [:recipes active-recipe :img] img)
      :dispatch [:recipes/close-modal]})))
