(ns playground.upload-file.views.upload-file-page
  (:require
   ;; import UploadFileIcon from '@mui/icons-material/UploadFile'
   ["@mui/icons-material/UploadFile" :default UploadFileIcon]
   ["@mui/material" :refer [Grid Typography Input IconButton Box Button
                            Card colors CardMedia]]
   ["mui-file-input" :refer [MuiFileInput]]
   [playground.components.page-nav :refer [page-nav]]
   [reagent.core :as r]
   [goog.dom :as gdom]
   [re-frame.core :as rf]
   [playground.components.modal :refer [modal]]))

(defn title
  []
  [:> Box {:position "relative"
           :border-radius "12px"
           :width 400
           :mb 3}
   [:> Box {:sx {:width 400
                 :height 50
                 :display "flex"
                 :align-items "center"
                 :justify-content "center"
                 :bgcolor (get-in (js->clj colors :keywordize-keys true) [:indigo :50])
                 :border 1
                 :border-radius 1}
            :my "auto"}
    [:> Typography
     {:variant "h5"}
     "Demo"]]])

(defn upload-button
  [handle-change]
  [:> Button
   {:variant "contained"
    :component "label"
    :color "primary"
    :on-change #(handle-change %)}
   [:> UploadFileIcon
    {:sx {:mr 1}}]
   [:> Input
    {:type "file"
     :id "input"
     :placeholder "Insert File"
     :style {:display "none"}}]
   "Upload File"])

(defn preview-image
  [path]
  [:> Card {:sx {:mt 2
                 :mx "auto"
                 :as "a"}
            :on-click #(rf/dispatch [:recipes/open-modal :upload-file-modal])}
   [:> CardMedia {:sx {:height 250
                       :width 350}
                  :image (str (or
                               path
                               "../img/placeholder.jpg"))
                  :alt name}]])

(defn preview-image-modal
  [& {:keys [height width]}]
  (fn []
    [:> Card {:sx {:mt 2
                   :mx "auto"}}
     [:> CardMedia {:sx {:height (or height 1080)
                         :width (or width 1200)}
                    :image (str (or
                                 @(rf/subscribe [:upload/latest-upload])
                                 "../img/placeholder.jpg"))
                    :alt name}]]))

(defn upload-file-page
  []
  (let [handle-change (fn [e]
                        (let [file (first (.-files (.-target e)))]
                          (rf/dispatch [:http/upload-file file])))]

    (fn []
      (let [path @(rf/subscribe [:upload/latest-upload])]
        [:<>
         [page-nav {:center "Upload File Example"}]
         [:> Card {:sx {:width 400
                        :height 400
                        :margin "auto"}}
          [:> Grid {:container true
                    :direction "column"
                    :align-items "center"
                    :justify-content "center"}
           [title]
           [upload-button handle-change]
           [preview-image path]
           [modal {:modal-key :upload-file-modal
                   :title "Zoomed preview"
                   :body
                   [preview-image-modal
                    {:height "30rem"
                     :width "50rem"}]
                   :footer
                   [:> Grid {:container true
                             :my 2
                             :px 3
                             :direction "row"
                             :align-items "center"
                             :justify-content "right"}
                    [:> Button {:variant "contained"
                                :color "primary"
                                :on-click #(rf/dispatch [:recipes/close-modal])}
                     "Close"]]}]]]]))))
(comment
  (let [el (gdom/getElement "input")
        file (-> el
                 (.-files)
                 (aget 0))
        form-data (doto (js/FormData.)
                    (.append "file" file))]
    (js/console.log form-data)))
