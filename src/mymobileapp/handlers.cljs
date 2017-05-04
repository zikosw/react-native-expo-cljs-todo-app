(ns mymobileapp.handlers
  (:require
    [re-frame.core :refer [reg-event-db ->interceptor]]
    [clojure.spec :as s]
    [mymobileapp.db :as db :refer [app-db]]))

;; -- Interceptors ----------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/develop/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (->interceptor
        :id :validate-spec
        :after (fn [context]
                 (let [db (-> context :effects :db)]
                   (check-and-throw ::db/app-db db)
                   context)))
    ->interceptor))

;; -- Helpers -----------------------------------------------------------------

(defn allocate-next-id
  "Returns the next todo id.
  Assumes todos are sorted.
  Returns one more than the current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
  :initialize-db
  [validate-spec]
  (fn [_ _]
    app-db))

(defn add-todo [db text]
  (let [todos (:todo/todos db)
        new-todo-id (allocate-next-id todos)]
    (assoc-in db [:todo/todos new-todo-id]
              #:todo{:id new-todo-id
                     :title text
                     :done false})))

(reg-event-db
  :add-todo
  [validate-spec]
  (fn [db [_ text]]
    (add-todo db text)))

(reg-event-db
  :toggle-todo
  []
  (fn [db [_ todo]]
    (let [todo-id (:todo/id todo)]
      (update-in db [:todo/todos todo-id :todo/done] not))))

(reg-event-db
  :remove-todo
  []
  (fn [db [_ todo]]
    (let [todo-id (:todo/id todo)]
      (assoc db :todo/todos (-> (get db :todo/todos)
                                (dissoc todo-id))))))
