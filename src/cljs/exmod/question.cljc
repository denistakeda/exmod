(ns exmod.question
  (:require [clojure.spec.alpha :as s]))

(defprotocol Question
  "General protocol for questions"
  (answered? [q] "Is question answered?")
  (answered-correctly? [q] "Is question answered correctly?")
  (answer [q n] "Answer this question")
  (unanswer [q n] "Undone your answer")
  (view [q on-select-answ on-unselect-answ] "View for this question"))

;; -- Specification --

(defmulti question-type type)
(s/def ::question (s/multi-spec question-type type))

(s/fdef answered?
  :args (s/cat :q ::question)
  :ret boolean?)

(s/fdef answered-correctly?
  :args (s/cat :q ::question)
  :ret boolean?)

(s/fdef answer
  :args (s/cat :q ::question
               :n nat-int?)
  :ret ::question)

(s/fdef unanswer
  :args (s/cat :q ::question
               :n nat-int?)
  :ret ::question)

(s/fdef view
  :args (s/cat :q ::question
               :on-select-answer keyword?
               :on-unselect-answer keyword?)
  :ret vector?)
