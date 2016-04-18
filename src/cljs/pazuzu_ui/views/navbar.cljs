(ns pazuzu-ui.views.navbar
  (:require [re-frame.core :refer [subscribe dispatch]]
            [pazuzu-ui.routes :refer [url-for]]))

(defn navbar []
  [:div.ui.attached.stackable.menu
   {:style {:margin-bottom "2em"}}
   [:div.ui.container
    [:a.item {:href (url-for :home-page)}
     [:i.fa.fa-bomb {:style {:margin-right 5}}]
     "Pazuzu"]

    [:a.item {:href (url-for :registry-page)}
     "Registry"]

    [:div.right.menu
     [:a.ui.item {:href (url-for :about-page)}
      "About"]]]])

