(ns pazuzu-ui.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub :registry
  (fn [db _] (reaction (:registry @db))))

(register-sub :ui-state
  (fn [db [_ & args]] (reaction (get-in (:ui @db) args))))
