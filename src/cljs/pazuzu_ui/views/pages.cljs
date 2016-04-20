(ns pazuzu-ui.views.pages
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn welcome []
  [:div.ui.vertical.masthead.center.aligned.segment
   {:style {:min-height "500px"}}
   [:div.ui.test.container
    [:h1.header {:style {:margin-top "2em"
                         :font-size "3em"}}
     "Welcome to Pazuzu UI"]
    [:h3 "Web application to operate docker features"]
    [:a.ui.huge.primary.button {:href "/api/auth"}
     "Authorize" [:i.icon.right.arrow]]]])

(defn about []
  [:div.ui.text.container
   [:h2 "About Pazuzu"]
   [:p "An evil ancient moster"]])

(defn feature-list-item [name]
  [:div.ui.card.feature
   [:div.content
    [:img.right.floated.ui.image {:src "https://placekitten.com/g/50/50"}]
    [:div.header name]
    [:div.meta "Dependencies:" [:a {:href "#"} "python"]]
    [:div.description "Feature description"]]])

(defn feature-details [name]
  [:div.ui.huge.input
   [:input {:type "text" :placeholder "Feature Name" :value name}]])

(defn registry []
  [:div#registry.ui.padded.grid

   [:div#features-pane.eight.wide.column
    [:div.ui.secondary.menu
     [:div.ui.icon.input
      [:input {:type "text" :placeholder "search"}]
      [:i.search.link.icon]]
     [:div.right.menu
      [:div.item [:div.ui.primary.button "New"]]]]

    [:div.features-list.ui.cards
     (for [i (range 100)] [feature-list-item (str "feature " i)])]]

   [:div#feature-details.eight.wide.column
    [:div.ui.secondary.menu
     [:div.right.menu
      [:div.item [:div.ui.labeled.icon.button.positive [:i.save.icon] "Save"]]
      [:div.item [:div.ui.labeled.icon.button.negative [:i.delete.icon] "Delete"]]]]

    [feature-details "Supa Dupa Feature"]]])

(defn active-page []
  (let [page (subscribe [:active-page])]
    (fn []
      [:div#page.ui.container
       (case @page
         :home-page     [welcome]
         :about-page    [about]
         :registry-page [registry]
         :not-found     [:div [:h1 "404?!"]]

         ;; default is wtf, unknown routes should be covered abouve
         [:div [:h1 "Internal problem with routes"]])])))