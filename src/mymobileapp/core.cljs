(ns mymobileapp.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [mymobileapp.handlers]
              [mymobileapp.subs]
              [mymobileapp.shared.ui :as ui]
              [cljs-exponent.reagent :refer [text text-input view image touchable-highlight touchable-native-feedback] :as rn]))

(defn todo-del-btn [todo]
  [touchable-highlight
    {:style {:background-color "lightpink"}
     :onPress #(dispatch [:remove-todo todo])}
    [text {:style {:padding 5}} "X"]])

(defn todo-item [attr]
  (let [todo (:data attr)]
    [touchable-highlight
      {:onPress #(dispatch [:toggle-todo todo])
       :underlayColor "aquamarine"}
      [view {:style {:flex-direction "row"
                     :justify-content "space-around"
                     :margin-bottom 5}}
        [text {} todo]
        [todo-del-btn todo]]]))

(defn showing-button [{:keys [showing value]} title]
  [touchable-highlight
   {:onPress #(dispatch [:set-showing value])
    :underlayColor "steelblue"
    :style {:flex 1
            :padding 10
            :background-color (if (= showing value) "skyblue" "white")}}
   [text
    {:style {:text-align "center"}}
    title]])

(defn app-root []
  (let [todos (subscribe [:todo/visible-todos])
        showing (subscribe [:todo/showing])]
    (fn []
      [view {:style {:flex-direction "column" :margin 10 :margin-top 30 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 10 :text-align "center"}} "Todo"]
       ;; New todo
       [text-input {:style {:width 200 :height 40 :text-align "center"}
                    :autoFocus true
                    :onSubmitEditing #(dispatch [:add-todo (.-text (.-nativeEvent %))])}]
       ;; Todo list
       [view {:style {:flex-direction "column"}}
         (for [todo @todos]
           [todo-item {:key (:todo/id todo) :data todo}])]
       ;; Filter
       [view {:style {:flex-direction "row" :margin-top 20}}
        [showing-button {:value :all :showing @showing}
          "All"]
        [showing-button {:value :active :showing @showing}
          "Active"]
        [showing-button {:value :done :showing @showing}
          "Done"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent rn/app-registry "main" #(r/reactify-component app-root)))
