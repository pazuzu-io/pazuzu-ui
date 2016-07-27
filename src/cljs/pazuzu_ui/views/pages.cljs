(ns pazuzu-ui.views.pages
  "Contains top-level navigation for various app's pages and basic route-handling"
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [subscribe dispatch]]
            [pazuzu-ui.authentication.core :refer (authenticate)]
            [pazuzu-ui.registry.view :as registry]
            [taoensso.timbre :as log :include-macros true]
            [pazuzu-ui.views.message :refer [simple-message-list]]))

(defn welcome [authenticated?]
  [:div.ui.vertical.masthead.center.aligned.segment
   {:style {:min-height "500px"}}
   [:div.ui.test.container
    [:h1.header {:style {:margin-top "2em"
                         :font-size  "3em"}}
     "Welcome to Pazuzu UI"]
    [:h3 "Web application to operate docker features"]
    (when-not authenticated?
      [:a.ui.huge.primary.button
       {:on-click authenticate}
       "Authorize" [:i.icon.right.arrow]])]])

(defn about []
  [:div.ui.text.container
   [:h2 "About Pazuzu"]
   [:p "An evil ancient moster"]])

(defn active-page []
  (let [page  (subscribe [:ui-state :active-page])
        messages (subscribe [:ui-state :messages])
        authentication (subscribe [:authentication])
        token (reaction (-> @authentication
                            :token))
        authorized? (not (= "not-found" @token))]
    (fn []
      (log/debug "Token" @token)
      (log/debug "authorized?" authorized?)
      [:div#page.ui.container
       [simple-message-list @messages]
       (case (:handler @page)
         :home-page [welcome authorized?]
         :about-page [about]
         :registry-page [registry/page]
         :registry-page-feature [registry/page]
         :not-found [:div [:h1 "404?!"]]
         ;; default is wtf, unknown routes should be covered above
         [:div [:h1 "Internal problem with routes"]])])))
