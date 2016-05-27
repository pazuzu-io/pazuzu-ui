(ns pazuzu-ui.config
  (:require-macros [adzerk.env :as env]))

(def debug? ^boolean js/goog.DEBUG)

(when debug?
  (enable-console-print!))

;;load environment variable or get the default one
(env/def
  API "http://localhost:8080")

(def registry-api API)