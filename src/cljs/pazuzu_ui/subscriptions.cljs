(ns pazuzu-ui.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

; Allows you to subscribe to changes of application data
; (not related to user interface)
(register-sub :registry
  (fn [db _] (reaction (:registry @db))))

; Allows you to subscribe to certain subset of UI state
(register-sub :ui-state
  (fn [db [_ & path]] (reaction (get-in (:ui-state @db) path))))