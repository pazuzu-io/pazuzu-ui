(ns pazuzu-ui.views.message
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn simple-message
  "Semantic UI Message with header and text description that can be autoclosed after some seconds"
  [id type header message]
    [:div.ui.message {:class-name type}
     [:i.close.icon {:on-click #(dispatch [:remove-message id])}]
     [:div.header header]
     [:p message]])

(defn simple-message-list
  "It will process a list of messages that be expected formed by {type header message time} and render a simple-message for each one"
  [messages]
  [:div.message-list
    (map
      (fn [msg]
        ^{:key (:id msg)}
        [simple-message (:id msg) (:type msg) (:header msg) (:message msg)]) messages)])
