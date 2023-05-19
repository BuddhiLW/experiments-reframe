(ns playground.nav.events
  (:require
   [playground.router :as router]
   [playground.spec :refer [check-spec-interceptor]]
   [re-frame.core :refer [path reg-event-db reg-event-fx reg-fx]]))

(def nav-interceptors [check-spec-interceptor
                       (path :nav)])
                       ;; check-spec-interceptor])

(reg-fx
 :navigate-to
 (fn [{:keys [path]}]
   (router/set-token! path)))

(reg-event-fx
 :route-changed
 nav-interceptors
 (fn [{nav :db} [_ {:keys [handler route-params]}]]
   (let [nav              (assoc nav :active-page handler)
         log-route-params (js/console.log (str "route-params: " route-params))
         log-nav          (js/console.log (str "nav: " nav))
         log-handler      (js/console.log (str "handler: " handler))]
     (case handler
       :recipes {:db       nav
                 :dispatch [:http/get-recipes]}

       :recipe {:db       (assoc nav :active-recipe (keyword (:recipe-id route-params)))
                :dispatch [:http/get-recipes]}

       :inbox
       {:db (assoc nav :active-inbox (keyword (:inbox-id route-params)))}

       {:db (do
              (js/console.log (str '(dissoc nav :active-recipe :active-inbox)
                                   (dissoc nav :active-recipe :active-inbox)))
              (js/console.log (str "nav: " nav))
              (dissoc nav :active-recipe :active-inbox))}))))

(reg-event-db
 :set-active-nav
 nav-interceptors
 (fn [nav [_ active-nav]]
   (assoc nav :active-nav active-nav)))

(reg-event-db
 :set-active-page
 nav-interceptors
 (fn [nav [_ active-page]]
   (assoc nav :active-page active-page)))

(reg-event-db
 :recipes/close-modal
 nav-interceptors
 (fn [nav _]
   (assoc nav :active-modal nil)))

(comment)
 ;; (rf/dispatch [:set-active-nav :id-nav]))
 ;; (fn nav [a b]]) -> (a <-> :set-active-nav) ^ (b <-> :id-nav))
