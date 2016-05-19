(ns pazuzu-ui.config)

(def debug? ^boolean js/goog.DEBUG)

(when debug?
  (enable-console-print!))

(def registry-api "http://localhost:8080")