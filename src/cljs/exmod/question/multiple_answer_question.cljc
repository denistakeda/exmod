(ns exmod.question.multiple-answer-question
  (:require [exmod.question :as question :refer [Question question-type]]
            [clojure.spec.alpha :as s]
            [re-frame.core :refer [dispatch]]
            [exmod.utils :refer [index-in-range?]]))

(declare answer-view MultipleAnswerQuestion)

;; -- Specification --

(s/def ::text string?)
(s/def ::answers (s/coll-of string? :min-count 2))
(s/def ::correct (s/coll-of nat-int? :min-count 1 :kind set?))
(s/def ::selected (s/coll-of nat-int? :kind set?))

(defmethod question-type MultipleAnswerQuestion [_]
  (s/and (s/keys :req-unq [::text ::answers ::correct ::selected])

         ;; Each correct answer should be in the list of answers
         (fn [{:keys [answers correct]}] (every? #(index-in-range? % answers) correct))

         ;; Each selected answer should be in the list of answers
         (fn [{:keys [answers selected]}] (every? #(index-in-range? % answers) selected))))

;; -- Record definition --

(defrecord MultipleAnswerQuestion [text answers correct selected]
  Question

  (answered? [q]
    (not-empty (:selected q)))

  (answered-correctly? [q]
    (= (:correct q) (:selected q)))

  (answer [q n]
    (update q :selected conj n))

  (unanswer [q n]

    (update q :selected
            (fn [col] (into #{} (remove #(= % n) col)))))

  (view [{:keys [text answers correct selected]} on-select-answ on-unselect-answ]
    [:div.question
     [:div.question-text text]
     [:div.question-answers
      (map-indexed (partial answer-view selected on-select-answ on-unselect-answ) answers)]]))

(defn answer-view [selected-ids on-select-answ on-unselect-answ idx answer]
  (let [selected? (contains? selected-ids idx)]
    ^{:key answer}
    [:div.answer
     {:on-click #(dispatch [(if selected? on-unselect-answ on-select-answ) idx])
      :class (if selected? "selected")}
     answer]))

;; -- Constructor --

(s/fdef multiple-answer-question
  :args (s/cat :text    ::text
               :answers ::answers
               :correct (s/* nat-int?))
  :ret ::question/question)

(defn multiple-answer-question [text answers & correct]
  (->MultipleAnswerQuestion text answers (into #{} correct) #{}))
