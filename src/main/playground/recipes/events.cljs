(ns playground.recipes.events
  (:require
   [ajax.core :as ajax]
   [clojure.walk :as w]
   [day8.re-frame.http-fx]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [playground.spec :refer [check-spec-interceptor]]
   [re-frame.core :as rf]
   [playground.helpers :as h]))
(def recipes-interceptors [check-spec-interceptor])

;; (def recipes-endpoint "https://gist.githubusercontent.com/jacekschae/50ffe6e8851a5dfe35e932682ca32d85/raw/06e8041d0abf86e2c5d809a334cf8f18d3d6303b/recipes.json")
;; (def base "http://localhost:8666")
(def recipes-endpoint "http://localhost:8666/v1/recipes")

(defn keywordize-id
  [coll]
  (->> coll
       (w/keywordize-keys)
       (map (fn [v]
              [(keyword (:id v)) (update v :id #(keyword (:id v)))]))
       (into {})))

(rf/reg-event-fx
 :http/get-recipes
 recipes-interceptors
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:loading :recipes] true)
    :http-xhrio {:method          :get
                 :uri             recipes-endpoint
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:recipes/set-recipes]
                 :on-failure      [:error/endpoint-request :get-recipes]}}))

(rf/reg-event-db
 :recipes/set-recipes
 (fn-traced [db [_ recipes]]
            (let [public-recipes (:public recipes)]
              (-> db
                  (assoc-in [:loading :recipes] false)
                  (assoc :recipes (h/associate-by-kkeyword-value :recipe/recipe_id public-recipes))))))

(rf/reg-event-fx
 :error/endpoint-request
 (fn-traced [db [_ request-type response]]
            (js/console.error "error" response)
            (-> db
                (assoc-in [:errors request-type] (get response :status-text))
                (assoc-in [:loading request-type] false))))

#_(rf/reg-event-db
   :http/complete-request
   (fn-traced [db [_ request-type]]
              (assoc-in db [:loading request-type] false)))

(rf/reg-event-db
 :recipes/save-recipe
 recipes-interceptors
 (fn-traced [db [_ recipe-id]]
            (let [uid (get-in db [:auth :uid])]
              (-> db
                  (update-in [:users uid :saved] conj recipe-id)
                  (update-in [:recipes recipe-id :saved-count] inc)))))

(rf/reg-event-db
 :recipes/unsave-recipe
 recipes-interceptors
 (fn-traced [db [_ recipe-id]]
            (let [uid (get-in db [:auth :uid])]
              (-> db
                  (update-in [:users uid :saved] disj recipe-id)
                  (update-in [:recipes recipe-id :saved-count] dec)))))

(rf/reg-event-db
 :recipes/open-modal
 recipes-interceptors
 (fn-traced
  [db [_ modal-name]]
  (assoc-in db [:nav :active-modal] modal-name)))

(rf/reg-event-fx
 :recipe/save-ingredient
 recipes-interceptors
 (fn-traced
  [{:keys [db]} [_ {:keys [id name amount measure]}]]
  (let [recipe-id (get-in db [:nav :active-recipe])
        ingredients (get-in db [:recipes recipe-id :ingredients])
        order (or (get-in ingredients [id :order])
                  (inc (count ingredients)))]
    {:db (assoc-in db [:recipes recipe-id :ingredients id]
                   {:id id
                    :order order
                    :name name
                    :amount amount
                    :measure measure})
     :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/delete-ingredient
 recipes-interceptors
 (fn-traced
  [{:keys [db]} [_ ingredient-id]]
  (let [active-recipe (get-in db [:nav :active-recipe])]
    {:db (update-in db [:recipes active-recipe :ingredients] dissoc ingredient-id)
     :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/save-step
 recipes-interceptors
 (fn-traced [{:keys [db]} [_ {:keys [id desc]}]]
            (let [active-recipe (get-in db [:nav :active-recipe])
                  steps         (get-in db [:recipes active-recipe :steps])
                  new-order     (if (number? (get-in steps [id :order]))
                                  (get-in steps [id :order])
                                  (inc (count steps)))]
                  ;; data (js/console.log "data" {:id id
                  ;;                              :order new-order
                  ;;                              :desc desc})]

              {:db (assoc-in db [:recipes active-recipe :steps id]  {:id id
                                                                     :order new-order
                                                                     :desc desc})
               :dispatch-n [#_[:steps/reorder-steps]
                            [:recipes/close-modal]]})))

#_(rf/reg-event-db
   :steps/reorder-steps
   (fn-traced [db _]
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
 recipes-interceptors
 (fn-traced [{:keys [db]} [_ step-id]]
            (let [active-recipe (get-in db [:nav :active-recipe])]
              {:db (update-in db [:recipes active-recipe :steps] dissoc step-id)
               :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/save-recipe
 recipes-interceptors
 (fn-traced [{:keys [db]} [_ {:keys [name prep-time]}]]
            (let [active-recipe (get-in db [:nav :active-recipe])
                  id (or active-recipe (keyword (str "recipe-" (random-uuid))))
                  uid (get-in db [:auth :uid])
                  recipe {:recipe/recipe_id id
                          :recipe/name name
                          :recipe/prep_time (js/parseInt prep-time)
                          :recipe/uid uid
                          :recipe/public false}]
              {:db (update-in db [:recipes id] merge recipe)
               :dispatch-n [[:recipes/close-modal]
                            [:http/post-recipe recipe uid]]})))

(rf/reg-event-fx
 :recipe/delete-recipe
 recipes-interceptors
 (fn-traced [{:keys [db]} _]
            (let [recipe (get-in db [:nav :active-recipe])]
         ;; uid (get-in db [:auth :uid])]
              {:db (update db :recipes  dissoc recipe)
               :dispatch-n [[:set-active-page :recipes]
                            [:set-active-nav :recipes]]
               :navigate-to {:path "/recipes/"}})))

(rf/reg-event-fx
 :recipe/publish
 recipes-interceptors
 (fn-traced [{:keys [db]} [_ {:keys [price]}]]
            (let [active-recipe (get-in db [:nav :active-recipe])]
              {:db (update-in db [:recipes active-recipe] merge {:price (js/parseInt price)
                                                                 :public? true})
               :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/unpublish
 recipes-interceptors
 (fn-traced [{:keys [db]} _]
            (let [active-recipe (get-in db [:nav :active-recipe])]
              {:db (assoc-in db [:recipes active-recipe :public?] false)
               :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/upsert-image
 recipes-interceptors
 (fn-traced [{:keys [db]} [_ {:keys [img]}]]
            (let [active-recipe (get-in db [:nav :active-recipe])]
              {:db (assoc-in db [:recipes active-recipe :img] img)
               :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :http/post-recipe
 (fn [_ [_ recipe]]
   {:http-xhrio {:method      :post
                 :uri         (h/endpoint "v1" "recipes")
                 :format      (ajax/json-request-format)
                 :params      recipe
                 :on-success  [:success/post-recipe-success]
                 :on-failure  [:error/endpoint-request]}}))

(rf/reg-event-fx
 :http/post-recipe-mock
 (fn [_ [_ _]]
   {:http-xhrio {:method      :post
                 :uri         (h/endpoint "v1" "recipes")
                 :format      (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :params      {:recipe/recipe_id "recipe-1"
                               :recipe/name "recipe-1"
                               :recipe/prep_time 10
                               :recipe/img "https://wellandfull.com/wp-content/uploads/2016/02/WellandFull-4-21.jpg"
                               :recipe/uid "user-1"
                               :recipe/public false}
                 :on-success  [:success/post-recipe-success]
                 :on-failure  [:error/endpoint-request]}}))

(rf/reg-event-fx
 :success/post-recipe-success
 (fn-traced [_ [_ response]]
            (js/console.log "post-recipe-success" response)))

(rf/reg-event-fx
 :http/get-recipes-log
 (fn-traced [{:keys [db]} _]
            {:http-xhrio {:method          :get
                          :uri             recipes-endpoint
                          :response-format (ajax/json-response-format {:keywords? true})
                          :on-success      [:log/sucess]
                          :on-failure      [:error/endpoint-request :get-recipes]}}))

(rf/reg-event-fx
 :log/sucess
 (fn-traced [{:keys [db]} [_ response]]
            (js/console.log "log/sucess" response)))
            ;; {:db (assoc-in db [:loading :recipes] false)
            ;;  :dispatch [:recipes/set-recipes response]}))

#_(rf/reg-event-fx
   :http/stripe-checkout
   :<- [:uid]
   (fn [{:keys [db uid] :as map} _]
     (js/console.log "http/stripe-checkout" map)
     (let [price (get-in db [:checkout :price])]
       {:db (-> db
                (assoc-in [:nav :checkout] true)
                (assoc-in [:checkout :price] 0))
        :http-xhrio {:method      :post
                     :uri         (h/endpoint "v1" "checkout")
                     :format      (ajax/json-request-format)
                     :params      {:uid uid
                                   :price price}
                     :on-success  [:success/stripe-checkout-success]
                     :on-failure  [:error/endpoint-request]}})))

(comment
  (ajax/POST (h/endpoint "v1" "recipes")
    {:params {:name "Pasta Carbonara"
              :prep-time 10
              :img ""}
     :handler (fn [response] (js/console.log "response success" response))
     :error-handler (fn [response] (js/console.error "response error" response))}))
