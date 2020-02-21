(ns exmod.question.multiple-answer-question
  (:require [exmod.question :refer [Question]]
            [clojure.spec.alpha :as s]
            [re-frame.core :refer [dispatch]]))

(declare answer-view)

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

(defn multiple-answer-question [text answers & correct]
  (->MultipleAnswerQuestion text answers (into #{} correct) #{}))
