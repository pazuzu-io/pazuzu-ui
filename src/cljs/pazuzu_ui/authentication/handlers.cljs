(ns pazuzu-ui.authentication.handlers
  (:require [re-frame.core :refer (register-handler)]
            [pazuzu-ui.database :as db]))

; set the authentication token
(register-handler :set-authentication-token
                  (fn [db [_ token]]
                    (assoc-in db [:authentication :token] token)))

; clear the token, i.e. logout
(register-handler :clear-authentication-token
                  (fn [db [_ token]]
                    (assoc-in db [:authentication :token] "not-found")))
