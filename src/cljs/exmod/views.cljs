(ns exmod.views
  (:require
   [re-frame.core :as re-frame]
   [exmod.subs :as subs]
   [exmod.quiz :as quiz]
   ))

(defn main-panel []
  (let [qz (re-frame/subscribe [::subs/quiz])]
    [:div.app
     [quiz/view @qz]]))
