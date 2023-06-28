(ns playground.nav.views.authenticated
  (:require
   ["@mui/material" :refer [Box Button Grid Typography]]
   [playground.nav.views.nav-item :refer [box]]
   [playground.router :as router]
   [re-frame.core :as rf]))

(defn authenticated
  []
  (let [active-page @(rf/subscribe [:active-page])
        nav-items [{:id :stripe
                    :name "Pay-wall"
                    :href (router/path-for :free-paywall)
                    :dispatch #(rf/dispatch [:set-active-nav :free-paywall])}
                   {:id :stripe
                    :name "Stripe"
                    :href (router/path-for :stripe)
                    :dispatch #(rf/dispatch [:set-active-nav :stripe])}
                   {:id :upload-file
                    :name "Upload File"
                    :href (router/path-for :upload-file)
                    :dispatch #(rf/dispatch [:set-active-nav :upload-file])}
                   {:id :saved
                    :name "Saved"
                    :href (router/path-for :saved)
                    :dispatch #(rf/dispatch [:set-active-nav :saved])}
                   {:id :recipes
                    :name "Recipes"
                    :href (router/path-for :recipes)
                    :dispatch #(rf/dispatch [:set-active-nav :recipes])}
                   {:id :inboxes
                    :name "Inbox"
                    :href (router/path-for :inboxes)
                    :dispatch #(rf/dispatch [:set-active-nav :inboxes])}
                   {:id :become-a-chef
                    :name "Chef"
                    :href (router/path-for :become-a-chef)
                    :dispatch #(rf/dispatch [:set-active-nav :become-a-chef])}
                   {:id :profile
                    :name "Profile"
                    :href (router/path-for :profile)
                    :dispatch #(rf/dispatch [:set-active-nav :profile])}]]
    [:<>
     [:> Box {:display "flex"
              :justify-content "flex-end"
              :py 1}
      (for [{:keys [id name href dispatch]} nav-items]
        [box {:key id
              :id id
              :name name
              :href href
              :dispatch dispatch
              :active-page active-page}])]]))
