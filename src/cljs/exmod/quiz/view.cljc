(ns exmod.quiz.view
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :refer [dispatch]]
            [exmod.question :as question]))

(declare quiz-view score-view navigation-view finish-button)

;; -- Data definitions --

(s/def ::fully-answered boolean?)
(s/def ::has-previous-question? boolean?)
(s/def ::has-next-question? boolean?)
(s/def ::current-number (s/and int? #(>= % 0)))
(s/def ::questions-count (s/and int? #(>= % 0)))
(s/def ::current-question record?)
(s/def ::scored? boolean?)

(s/def ::on-finish-quiz keyword?)
(s/def ::on-next-question keyword?)
(s/def ::on-previous-question keyword?)
(s/def ::on-select-question keyword?)
(s/def ::on-answer-current keyword?)
(s/def ::on-unanswer-current keyword?)

(s/def ::full-data
  (s/keys :req [::fully-answered
                ::has-previous-question?
                ::has-next-question?
                ::current-number
                ::questions-count
                ::current-question
                ::scored?

                ::on-finish-quiz
                ::on-next-question
                ::on-previous-question
                ::on-select-question
                ::on-answer-current
                ::on-unanswer-current]))

;; -- Views --

(s/fdef view
  :args (s/and (s/cat :data ::full-data))
  :ret vector?)

(defn view [data]
  (if (::scored? data)
    (score-view data)
    (quiz-view data)))


(s/fdef quiz-view
  :args (s/and (s/cat :data ::full-data))
  :ret vector?)

(defn- quiz-view
  [{:keys [::current-question
           ::on-answer-current
           ::on-unanswer-current]
    :as data}]
  [:div.quiz
   (navigation-view data)
   (finish-button data)
   [question/view current-question on-answer-current on-unanswer-current]])

(defn- navigation-view
  "Navigation panel"
  [{:keys [::current-number
           ::questions-count
           ::has-next-question?
           ::has-previous-question?

           ;; Handlers
           ::on-select-question
           ::on-next-question
           ::on-previous-question] :as data}]

  [:div.quiz-navigation
   [:button {:on-click #(dispatch [on-previous-question])
             :disabled (not has-previous-question?)}
    "<<"]

   (for [i (range questions-count)]
     ^{:key i} [:div.quiz-navigation-item {:on-click #(dispatch [on-select-question i])
                                           :class (if (= i current-number) "selected")}
                (str (inc i))])

   [:button {:on-click #(dispatch [on-next-question])
             :disabled (not has-next-question?)}
    ">>"]])

(defn- finish-button [{:keys [fully-answered? on-finish]}]
  (when fully-answered?
    [:button.finish-button {:on-click on-finish} "Finish"]))

(defn- score-view [quiz]
  [:div.score "Score view"])
