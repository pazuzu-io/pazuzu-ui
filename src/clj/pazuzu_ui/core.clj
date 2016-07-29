(ns pazuzu-ui.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.adapter.jetty :refer :all]
            [ring.util.response :as resp]
            [ring.middleware.resource :as resource]
            [ring.middleware.cookies :refer (wrap-cookies)]
            [ring.middleware.reload :refer (wrap-reload)]
            [adzerk.env :as env]
            [cheshire.core :refer (parse-string)]))

(env/def CREDENTIALS_DIR "./credentials")

(def credentials-dir CREDENTIALS_DIR)

(defn index-page
  [request]
  (let [response {:status 200
                  :headers {"Content-Type" "text/html; charset=utf-8" }
                  :cookies {"client_id" {:value (:client-id request "not-found")}}
                  :body (slurp "resources/public/index.html")}]
    (println (:cookies response))
    response))

(defroutes
  app-routes
  (GET "/*" request (index-page request)))

(defn read-client-id-from-fs []
  (some-> credentials-dir
          (str "/client.json")
          slurp
          (parse-string true)
          :client_id))

(def read-client-id (memoize read-client-id-from-fs))

(defn inject-client-id
  "Simple middleware to inject the OAuth clientid"
  [handler]
  (fn [request]
    (if-let [client-id (read-client-id)]
      (handler (assoc request :client-id client-id))
      (handler request))))

;; Ring handler for development
(def app-dev
  (-> #'app-routes
      wrap-reload
      inject-client-id
      wrap-cookies
      (resource/wrap-resource "public")))

;; Ring handler
(def app
  (-> app-routes
      inject-client-id
      wrap-cookies
      (resource/wrap-resource "public")))

(defn -main []
  (run-jetty app {:port 8080}))
