(ns pazuzu-ui.views.pages
  "Contains top-level navigation for various app's pages and basic route-handling"
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [subscribe]]
            [pazuzu-ui.registry.view :as registry]
            [pazuzu-ui.views.message :refer [simple-message-list]]))

(defn welcome []
  [:div.ui.vertical.masthead.center.aligned.segment
   {:style {:min-height "500px"}}
   [:div.ui.test.container
    [:h1.header {:style {:margin-top "2em"
                         :font-size  "3em"}}
     "Welcome to Pazuzu UI"]
    [:h3 "Web application to operate docker features"]
    [:a.ui.huge.primary.button {:href "/api/auth"}
     "Authorize" [:i.icon.right.arrow]]]])

(defn about []
  [:div.ui.text.container
   [:h2 "About Pazuzu"]
   [:p "An evil ancient moster"]])

(defn active-page []
  (let [page  (subscribe [:ui-state :active-page])
        messages (subscribe [:ui-state :messages])]
    (fn []
      [:div#page.ui.container
       [simple-message-list @messages]
       (case (:handler @page)
         :home-page [welcome]
         :about-page [about]
         :registry-page [registry/page]
         :registry-page-feature [registry/page]
         :not-found [:div [:h1 "404?!"]]
         ;; default is wtf, unknown routes should be covered abouve
         [:div [:h1 "Internal problem with routes"]])])))
