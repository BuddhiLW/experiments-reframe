(ns playground.components.modal
  (:require
   ["@mui/material" :refer [Card CardMedia Modal Typography Grid Button Box
                            FormControl colors]]
   [re-frame.core :as rf]))

(defn modal
  [{:keys [modal-key body footer title]}]
  (fn []
    (let [active-modal @(rf/subscribe [:recipes/active-modal])]
      (js/console.log "active-modal" @(rf/subscribe [:recipes/active-modal]))
      (js/console.log "active-modal" active-modal)
      (js/console.log "modal-key" modal-key)
      [:> Modal {:open (= active-modal modal-key)
                 :on-close #(rf/dispatch [:recipes/close-modal])}
       [:<>
        [:> Box {:sx
                 {:position "absolute"
                  :border-radius "12px"
                  :top "50%"
                  :left "50%"
                  :transform "translate(-50%, -50%)"
                  :width 800
                  :bgcolor (get-in (js->clj colors :keywordize-keys true) [:indigo :50])}}
         [:> Typography {:p 2
                         :variant "h4"
                         :align "center"
                         :font-weight 500
                         :class-name "text-slate-600"}
          title]
         [body]
         [footer]]]])))
