(ns pazuzu-ui.views
    (:require [pazuzu-ui.views.navbar :refer [navbar]]
              [pazuzu-ui.views.pages :refer [active-page]]))


(defn root-component
  "This is root component that will render whole application"
  []
  [:div#root
   [navbar]
   [active-page]])
