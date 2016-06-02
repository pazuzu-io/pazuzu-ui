(ns pazuzu-ui.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.adapter.jetty :refer :all]
            [ring.util.response :as resp]
            [ring.middleware.resource :as resource]))

(defroutes app-routes
           ; this helps with client-side routing during development
           (GET "/*" [] (resp/resource-response "index.html" {:root "public"}))
           )

;; Ring handler
(def app
  (resource/wrap-resource app-routes "public"))

(defn -main []
  (run-jetty app {:port 8080}))