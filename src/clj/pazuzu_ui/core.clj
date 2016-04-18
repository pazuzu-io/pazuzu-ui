(ns pazuzu-ui.core
  (:require [compojure.core :refer :all]
            [ring.util.response :as resp]))

(defroutes app
  ; this helps with client-side routing during development
  (GET "/*" [] (resp/resource-response "index.html" {:root "public"})))