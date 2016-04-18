(ns pazuzu-ui.views
    (:require [pazuzu-ui.views.navbar :refer [navbar]]
              [pazuzu-ui.views.pages :refer [active-page]]))


(defn root-component []
  [:div
   [navbar]
   [active-page]])
