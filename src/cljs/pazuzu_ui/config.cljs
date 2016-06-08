(ns pazuzu-ui.config
  (:require-macros [adzerk.env :as env]))

(def debug? ^boolean js/goog.DEBUG)

(when debug?
  (enable-console-print!))

;;load environment variable or get the default one, will be loaded at compile time
(env/def
  BACKEND_ENDPOINT "http://localhost:8080")

(def registry-api BACKEND_ENDPOINT)