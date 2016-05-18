(ns pazuzu-ui.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.database :as db]))

;;general purpose event handlers
(register-handler :initialize-db
                  (fn [_ _] db/default-db))

(register-handler :set-active-page
                  (fn [previous [_ active-page]]
                    (assoc-in previous [:ui-state :active-page] active-page)))