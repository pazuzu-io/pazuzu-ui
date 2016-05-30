(ns pazuzu-ui.views.loading-component)

(defn loading-component [loading? component]
  (if loading? [:div.ui [:div.ui.active.inverted.dimmer [:div.ui.loader]]] component))