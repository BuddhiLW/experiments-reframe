(ns playground.become-a-chef.views.become-a-chef
  (:require
   [playground.components.page-nav :refer [page-nav]]
   [playground.become-a-chef.views.agreement :refer [agreement]]
   ["@mui/material" :refer [Box Grid Typography ButtonIcon IconButton
                            Card CardMedia]]
   ["@mui/icons-material/CheckCircle" :default CheckCircleIcon]))

(defn become-a-chef
  []
  (let [steps [{:header "Create your recipes for free"
                :sub-header "Start by creating a draft. Add name and cooking time. Make your recipe stand out by adding picture and describe all needed ingredients and steps."}
               {:header "Publish and get noticed"
                :sub-header "Easily publish your recipes and allow people to get in touch with you via one click. Await a message for your first cooking event."}
               {:header "Cook for the first time"
                :sub-header "Talk to the interested person and agree on the date, time, and location. Show your best at the even and become a chef."}]]
    [:> Box
     [page-nav {:center "Become a Chef"
                :right [agreement]}]
     [:> Grid {:container true
               :direction "row"
               :spacing 2}
      [:> Grid {:item true
                :xs 6
                :display "flex"
                :justify-content "center"
                :align-items "center"}
       [:> Card {:sx {:maxwidth 500
                      :maxheight 500
                      :border-radius "100%"}
                 :class-name "rounded-full"
                 :variant "outlined"}
        [:> CardMedia {:component "img"
                       :maxheight 500
                       :maxwidth 500
                       :class-name "rounded-full"
                       :image "https://res.cloudinary.com/schae/image/upload/f_auto,q_auto/v1546889699/cheffy/become-a-chef.jpg"
                       :alt "become a chef"}]]]
      [:> Grid {:item true
                :xs 6
                :display "flex"
                :sx {:flex-direction "column"}
                :gap 3
                :justify-content "flex-start"}
       (for [{:keys [header sub-header]} steps]
         ^{:key header}
         [:> Card {:sx {:maxwidth "70%"}}
          [:> Box {:display "flex"
                   :justify-content "flex-start"
                   :maxwidth "95%"}
           [:> Box {:display "flex"
                    :justify-content "center"
                    :pl 2
                    :align-items "center"}
            [:> IconButton {:sx {:color "#27AB83"}}
             [:> CheckCircleIcon {:sx {:font-size "2em"}}]]]
           [:> Box {:p 7
                    :pl 3}
            [:> Grid
             [:> Typography {:variant "h4"}
              header]]
            [:> Grid
             [:> Typography {:variant "body1"}
              sub-header]]]]])]]]))
