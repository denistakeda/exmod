(ns exmod.question
  (:require [clojure.spec.alpha :as s]))

(defprotocol Question
  "General protocol for questions"
  (answered? [q] "Is question answered?")
  (answered-correctly? [q] "Is question answered correctly?")
  (answer [q n] "Answer this question")
  (unanswer [q n] "Undone your answer")
  (view [q on-select-answ on-unselect-answ] "View for this question"))
