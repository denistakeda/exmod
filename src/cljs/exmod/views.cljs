(ns exmod.views
  (:require
   [re-frame.core :as re-frame]
   [exmod.quiz.view :as quiz]
   [exmod.quiz.subs :as quiz-subs]
   ))

(defn main-panel []
  (let [qz (re-frame/subscribe [::quiz-subs/quiz-view])]
    [:div.app
     [quiz/view @qz]]))
