(ns pazuzu-ui.views.pagination
  (:require [re-frame.core :refer [dispatch]]))

(defn pagination [total-items per-page page page-change-event]
  (let [max-pages (Math/ceil (/ total-items per-page))]
    (if (> max-pages 1)
      [:div.ui.right.floated.pagination.menu
        [:a.icon.item {:on-click  (if (> page 1) #(dispatch [page-change-event (- page 1)]))}
          [:i.left.chevron.icon]]
        (doall
          (for [i (range max-pages)]
            [:a.item {:class-name (if (= page (+ i 1)) "active")
                      :key i
                      :on-click (if (not= page (+ i 1)) #(dispatch [page-change-event (+ i 1)]))}
                      (+ i 1)]))
        [:a.icon.item {:on-click (if (< page max-pages) #(dispatch [page-change-event (+ page 1)]))}
          [:i.right.chevron.icon]]]
        [:span]
    )))
