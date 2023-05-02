(ns playground.core
  (:require
   ;;--- Iniciate/mount the application ---
   [goog.dom :as gdom]
   [reagent.dom]
   ;; [reagent.core :as r]
   ;;--- State manager ---
   [re-frame.core :as rf]
   ;;--- main state: db ---
   [playground.db]
   ;;--- theming with Material-UI ---
   ["@mui/material" :as mui]
   [playground.theme :refer [cheffy-theme theme]]

   ;; -----------------------------------------
   ;;---- Bussiness logic for the front-end -------
   ;; -----------------------------------------

   ;--- auth ---
   [playground.auth.views.profile :refer [profile]]
   [playground.auth.views.sign-up :refer [sign-up]]
   [playground.auth.views.log-in :refer [log-in]]
   [playground.auth.events]
   [playground.auth.subs]
   ;--- become-a-chef ---
   [playground.become-a-chef.views.become-a-chef :refer [become-a-chef]]
   ;--- inbox ---
   [playground.inbox.views.inboxes :refer [inboxes]]
   ;;--- nav ---
   [playground.nav.views.nav :refer [nav]]
   [playground.nav.events]
   [playground.nav.subs]
   ;--- recipe ---
   [playground.recipe.views.recipes :refer [recipes]]))

;; ---------- END requires ---------
(defn pages
  [page-name]
  (case page-name
    :profile [profile]
    :sign-up [sign-up]
    :log-in [log-in]
    :become-a-chef [become-a-chef]
    :inbox [inboxes]
    :recipes [recipes]
    [recipes]))

(defn- main []
  (let [active-nav @(rf/subscribe [:active-nav])]
    [:div
     [:> mui/CssBaseline]
     [:> mui/ThemeProvider {:theme theme}
      [:> mui/Grid {:background-color "primary.main-background"}
       [:> mui/Box {:py 1
                    :component "nav"
                    :variant "dense"
                    :fluid "false"
                    :display "flex"
                    :justify-content "flex-end"
                    :container "true"}
        [:> mui/Box [nav]]]
       [pages active-nav]]]]))

;; -----------------------------------------------------------------------------
;; Mount logic
(defn- render []
  (reagent.dom/render [main] (gdom/getElement "app")))

(defn init
  []
  (rf/dispatch-sync [:initialize-db])
  (render))

(defn- ^:dev/after-load re-render
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (render))
