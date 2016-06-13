(ns pazuzu-ui.views.code-editor
  (:require
    [reagent.core :as reagent]
    [cljsjs.codemirror]))

(defn code-editor
  [mode text change-callback]
     (reagent/create-class
       {:component-did-mount
         (fn [comp] (let [cm (.fromTextArea
             js/CodeMirror
             (reagent/dom-node comp)
             #js {
               :mode mode
               :lineNumbers   true
             })]
             (reagent/set-state comp (assoc (reagent/state comp) :editor cm))
             (.on cm "change" (fn [cm change] (change-callback (.getValue cm))))))
        :display-name  (str "code-editor-" mode)
        :componentWillReceiveProps
          (fn [comp [_ _ textProp]]
            (let [editor (:editor (reagent/state comp))]
            (if
              (not= (.getValue editor) textProp)
              (.setValue
                editor
                (if (nil? textProp) "" textProp)))))
        :reagent-render
          (fn [mode text change-callback]
            [:textarea {:field     :textarea
                        :rows      3
                        :value     text
                        :on-change #(identity %)}])}))
