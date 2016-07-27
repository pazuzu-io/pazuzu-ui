(ns pazuzu-ui.core
  (:require-macros [reagent.ratom :refer (reaction)])
  (:require [taoensso.timbre :as log :include-macros true]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [pazuzu-ui.handlers]
            [pazuzu-ui.registry.handlers]
            [pazuzu-ui.subscriptions]
            [pazuzu-ui.routes :as routes]
            [pazuzu-ui.views :as views]
            [pazuzu-ui.config :as config]
            [pazuzu-ui.authentication.handlers]
            [pazuzu-ui.authentication.core :refer (get-token)]))

(when config/debug?
  (log/debug "Running app in dev mode"))

(defn mount-root []
  (reagent/render [views/root-component]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (routes/init-routes)
  ; check if we are not authenticated and if we need to get the token from the
  ; query string
  (let [auth-state (re-frame/subscribe [:authentication])
        token (reaction (-> @auth-state
                            :token))]
    (when (= "not-found" @token)
      (when-let [token (get-token)]
        (log/debug "We are authenticated")
        (re-frame/dispatch-sync [:set-authentication-token token]))))
  (mount-root))
