(ns pazuzu-ui.database)

; This is default state of the app
(def default-db
  {:name     "pazuzu-ui"
   :registry {:features []}
   :ui-state {
              :messages []
              :active-page   :home-page
              :registry-page {:search-input-value    ""
                              :selected-feature-name nil
                              :feature-pane          {:new-feature? false
                                                      :feature      nil}}}})