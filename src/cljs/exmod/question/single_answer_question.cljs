(ns exmod.question.single-answer-question
  (:require [exmod.question :refer [Question]]
            [exmod.quiz :as quiz]
            [re-frame.core :refer [dispatch]]))

(declare answer-view)

(defrecord SingleAnswerQuestion [text answers correct selected]
  Question

  (answered? [q]
    (not (nil? (:selected q))))

  (answered-correctly? [q]
    (= (:correct q) (:selected q)))

  (answer [q n]
    (assoc q :selected n))

  (unanswer [q n]
    (assoc q :selected nil))

  (view [{:keys [text answers correct selected]}]
    [:div.question
     [:div.question-text text]
     [:div.question-answers
      (map-indexed (partial answer-view selected) answers)]]))

(defn answer-view [selected-id idx answer]
  ^{:key answer} [:div.answer {:on-click #(dispatch [::quiz/answer-current idx])
                               :class   (if (= selected-id idx) "selected")}
                  answer])

(defn single-answer-question [text answers correct]
  (->SingleAnswerQuestion text answers correct nil))
