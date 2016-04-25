(ns pazuzu-ui.views.navbar
  "Namespace contains components that are related to navigational bar on top"
  (:require [re-frame.core :refer [subscribe dispatch]]
            [pazuzu-ui.routes :refer [url-for]]))

(defn navbar []
  [:div#navbar.ui.attached.stackable.menu
   [:div.ui.container
    [:a.item {:href (url-for :home-page)}
     [:i.fa.fa-bomb {:style {:margin-right 5}}]
     "Pazuzu"]

    [:a.item {:href (url-for :registry-page)}
     "Registry"]

    [:div.right.menu
     [:a.ui.item {:href (url-for :about-page)}
      "About"]]]])

