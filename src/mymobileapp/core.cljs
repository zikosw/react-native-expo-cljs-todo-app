(ns mymobileapp.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [mymobileapp.handlers]
              [mymobileapp.subs]
              [mymobileapp.shared.ui :as ui]
              [cljs-exponent.reagent :refer [text text-input view image touchable-highlight touchable-native-feedback] :as rn]))

(defn todo-del-btn [todo]
  [touchable-highlight
    {:style {:background-color "red"}
     :onPress #(dispatch [:remove-todo todo])}
    [text " X "]])

(defn todo-item [attr]
  (let [todo (:data attr)]
    [touchable-highlight
      {:onPress #(dispatch [:toggle-todo todo])
       :style {:align-self "stretch"}}
      [view {:style {:flex-direction "row"
                     :justify-content "space-around"
                     :margin-bottom 5}}
        [text {} todo]
        [todo-del-btn todo]]]))

(defn showing-button [showing title]
  [touchable-highlight
   {:onPress #(dispatch [:set-showing showing])}
   [text title]])

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
        [showing-button :all "All"]
        [showing-button :active "Active"]
        [showing-button :done "Done"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent rn/app-registry "main" #(r/reactify-component app-root)))
