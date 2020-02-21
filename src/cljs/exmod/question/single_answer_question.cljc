(ns exmod.question.single-answer-question
  (:require [exmod.question :as question :refer [Question question-type]]
            [re-frame.core :refer [dispatch]]
            [clojure.spec.alpha :as s]
            [exmod.utils :refer [index-in-range?]]))

(declare answer-view SingleAnswerQuestion)

;; -- Specification --

(s/def ::text string?)
(s/def ::answers (s/coll-of string? :min-count 2))
(s/def ::correct nat-int?)
(s/def ::selected nat-int?)

(defmethod question-type SingleAnswerQuestion [_]
  (s/and (s/keys :req-unq [::text ::answers ::correct]
                 :opt-unq [::selected])

         ;; Correct answer should be in the list of answers
         (fn [{:keys [answers correct]}] (index-in-range? correct answers))

         ;; Selected answer is either nil or in the list of answers
         (s/or :selected (fn [{:keys [answers selected]}] (index-in-range? selected answers))
               :unselected #(-> % :selected nil?))))

;; -- Record definition --

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

  (view [{:keys [text answers correct selected]} on-select-answ _]
    [:div.question
     [:div.question-text text]
     [:div.question-answers
      (map-indexed (partial answer-view selected on-select-answ) answers)]]))

(defn answer-view [selected-id on-select-answ idx answer]
  ^{:key answer} [:div.answer {:on-click #(dispatch [on-select-answ idx])
                               :class   (if (= selected-id idx) "selected")}
                  answer])

;; -- Constructor --

(s/fdef single-answer-question
  :args (s/cat :text    ::text
               :answers ::answers
               :correct ::correct)
  :ret ::question/question)

(defn single-answer-question [text answers correct]
  (->SingleAnswerQuestion text answers correct nil))
