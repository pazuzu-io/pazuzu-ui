(ns pazuzu-ui.routes
  (:require [taoensso.timbre :as log :include-macros true]
            [re-frame.core :refer [dispatch]]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]))

(log/debug "Registering routes")

(def routes ["/" {""         :home-page
                  "about"    :about-page
                  "registry" {"" :registry-page
                              "/" :registry-page
                              ["/" :name] :registry-page-feature}
                  true       :not-found}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [matched-route]
  (let [{handler :handler} matched-route]
    (dispatch [:set-active-page matched-route])))

(defn init-routes []
  (log/debug "Initializing routes")
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (partial bidi/path-for routes))
