(ns pazuzu-ui.views.loading-component)

(defn loading-component [loading? component]
  (if loading? [:div.ui.segment [:div.ui.active.inverted.dimmer [:div.ui.loader]]] component))