(ns mymobileapp.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [mymobileapp.handlers]
              [mymobileapp.subs]
              [mymobileapp.shared.ui :as ui]
              [cljs-exponent.reagent :refer [text text-input view image touchable-highlight touchable-native-feedback] :as rn]))

(def react-native (js/require "react-native"))
(def button (r/adapt-react-class (aget react-native "Button")))

(defn alert [title]
  (.alert rn/alert title))

(defn todo-del-btn [todo]
  [touchable-highlight
    {:style {:background-color "red"}
     :on-press #(dispatch [:remove-todo todo])}
    [text " X "]])

(defn todo-item [attr]
  (let [todo (:data attr)]
    (prn todo)
    [touchable-highlight
      {:on-press #(dispatch [:toggle-todo todo])
       :style {:align-self "stretch"}}
      [view {:style {:flex-direction "row"
                     :justify-content "space-around"
                     :margin-bottom 5}}
        [text {} todo]
        [todo-del-btn todo]]]))

(defn app-root []
  (let [todos (subscribe [:todo/visible-todos])]
    (fn []
      [view {:style {:flex-direction "column" :margin-top 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 10 :text-align "center"}} "Todo"]
       ;; New todo
       [text-input {:style {:width 200
                            :height 40
                            :text-align "center"}
                    :autoFocus true
                    :onSubmitEditing #(dispatch [:add-todo (.-text (.-nativeEvent %))])}]
       ;; Todo list
       (for [todo @todos]
         [todo-item {:key (key todo) :data (val todo)}])
       ;; Filter
       [view {:style {:flex-direction "row" :justify-content "space-around" :align-self "stretch"}}
         [touchable-highlight
           {:on-press #(prn "All~~")}
           [text "All"]]
         [touchable-highlight
           {:on-press #(prn "Active~~")}
           [text "Active"]]
         [touchable-highlight
           {:on-press #(prn "Complete~~")}
           [text "Completed"]]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent rn/app-registry "main" #(r/reactify-component app-root)))
