(ns exmod.quiz.view
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :refer [dispatch]]
            [exmod.question :as question]
            [exmod.quiz.events :as events]))

(declare quiz-view score-view navigation-view finish-button)

;; -- Data definitions --

(s/def ::fully-answered? boolean?)
(s/def ::has-previous-question? boolean?)
(s/def ::has-next-question? boolean?)
(s/def ::current-number (s/and int? #(>= % 0)))
(s/def ::questions-count (s/and int? #(>= % 0)))
(s/def ::current-question record?)
(s/def ::scored? boolean?)
(s/def ::score (s/and int? #(>= % 0)))

(s/def ::full-data
  (s/keys :req [::fully-answered?
                ::has-previous-question?
                ::has-next-question?
                ::current-number
                ::questions-count
                ::current-question
                ::scored?]
          :opt [::score]))

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
   [question/view current-question ::events/answer-current ::events/unanswer-current]])


(defn- navigation-view
  "Navigation panel"
  [{:keys [::current-number
           ::questions-count
           ::has-next-question?
           ::has-previous-question?] :as data}]

  [:div.quiz-navigation
   [:button {:on-click #(dispatch [::events/prev])
             :disabled (not has-previous-question?)}
    "<<"]

   (for [i (range questions-count)]
     ^{:key i} [:div.quiz-navigation-item {:on-click #(dispatch [::events/goto-question i])
                                           :class (if (= i current-number) "selected")}
                (str (inc i))])

   [:button {:on-click #(dispatch [::events/next])
             :disabled (not has-next-question?)}
    ">>"]])

(defn- finish-button [{:keys [::fully-answered?] :as data}]
  (when fully-answered?
    [:button.finish-button {:on-click #(dispatch [::events/finish-quiz])} "Finish"]))

(defn- score-view [{:keys [::score ::questions-count]}]
  [:div.score
   [:div
    [:span "Congratulations! You answered correctly "]
    [:span.user-score (str score)]
    [:span " Questions of "]
    [:span.total-score (str questions-count)]
    [:span "!"]]])
