(ns mymobileapp.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [mymobileapp.handlers]
              [mymobileapp.subs]
              [mymobileapp.shared.ui :as ui]
              [cljs-exponent.reagent :refer [text text-input view image touchable-highlight] :as rn]))

(defn alert [title]
  (.alert rn/alert title))

(defn todo-item [attr child]
  [text {:key (:key attr)} child])

(defn app-root []
  (let [todos (subscribe [:todos])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} "Todo App"]
       [text-input {:style {:width 200
                            :height 40
                            :text-align "center"}
                    :autoFocus true
                    :onSubmitEditing #(dispatch [:add-todo (.-text (.-nativeEvent %))])}]
       (map-indexed (fn [idx itm]
                      [todo-item {:key idx} itm])
                    @todos)])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent rn/app-registry "main" #(r/reactify-component app-root)))
