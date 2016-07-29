(ns pazuzu-ui.authentication.core
  (:require [cemerick.url :refer (map->URL query->map)]
            [re-frame.core :refer (subscribe)])
  (:import [goog Uri]))

(defn auth-endpoint []
  (let [client-id (second (re-find #"client_id=(.+)" js/document.cookie))]
    (str
     (map->URL
      {:protocol "https"
       :host "auth.zalando.com/"
       :path "oauth2/authorize"
       :query {:realm "employees"
               :redirect_uri "https://localhost:8080/"
               :response_type "token"
               :client_id client-id
               :scope "uid"}}))))

(defn authenticate []
  (set! js/window.location (auth-endpoint)))

(defn- get-from-fragment [key]
  (some-> (Uri. js/document.URL)
          .getFragment
          query->map
          (#(get % key))))

(defn get-token
  []
  (get-from-fragment "access_token"))
