(ns pazuzu-ui.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub :active-page
              (fn [db _] (reaction (:active-page @db))))
