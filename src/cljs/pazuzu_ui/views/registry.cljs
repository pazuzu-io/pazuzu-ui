(ns pazuzu-ui.views.registry
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]))

(defn feature-details [{:keys [feature]}]
  [:div.ui.grid.container
   [:div.row
    [:div.column
     [:div.ui.form
      [:div.field
       [:div.ui.huge.input.fluid
        [:input {:type "text" :placeholder "Feature Name"
                 :value (:name feature)
                 :on-change (fn [event]
                              (let [value (-> event .-target .-value)]
                                (log/debug value)))}]]]
      [:div.field
       [:label "Dependencies"]
       (for [dep (:dependencies feature)] [:div.ui.label {:key dep} dep])]
      [:div.field.code
       [:label "Snippet"]
       [:textarea {:field :textarea
                   :rows 5
                   :value (:snippet feature)
                   :on-change (fn [e] (log/debug e))}]]
      [:div.field.code
       [:label "Test Case command"]
       [:textarea {:field :textarea
                   :rows 3
                   :value (:test feature)
                   :on-change (fn [e] (log/debug e))}]]
      [:div.field
       [:label "Attached files"]
       [:div.ui.label "id-rsa.pub"]
       [:button.mini.ui.basic.button [:i.icon.upload] "Upload file"]]]]]])

(defn feature-list-item [{:keys [is-selected feature]}]
  [:div.ui.card.feature
   {:on-click #(dispatch [:select-feature (:name feature)])
    :class (if is-selected "selected" "not-selected")}
   [:div.content
    [:div.header (:name feature)]
    [:div.meta "Dependencies:"
     (for [dep (:dependencies feature)] [:a {:href "#" :key dep} dep])]
    [:div.description "Feature description"]]])

(defn page []
  (let [state (subscribe [:registry])
        features (reaction (-> @state :features vals))
        selected (reaction (get-in @state [:features (:selected @state)]))]
    (log/debug "Selected" @selected)
    (fn []
      [:div#registry.ui.padded.grid
       [:div#features-pane.eight.wide.column
        [:div.ui.secondary.menu
         [:div.ui.icon.input
          [:input {:type "text" :placeholder "search"}]
          [:i.search.link.icon]]
         [:div.right.menu
          [:div.item [:div.ui.primary.button
                      {:on-click (fn [] )}
                      "New"]]]]

        [:div.features-list.ui.cards
         (doall (for [feature @features]
                  [feature-list-item {:key (:name feature)
                                      :is-selected (= feature @selected)
                                      :feature feature}]))]]

       [:div#feature-details.eight.wide.column
        [:div.ui.secondary.menu
         [:div.right.menu
          [:div.item [:div.ui.labeled.icon.button.positive [:i.save.icon] "Save"]]
          [:div.item [:div.ui.labeled.icon.button.negative [:i.delete.icon] "Delete"]]]]

        [feature-details {:feature @selected}]]])))