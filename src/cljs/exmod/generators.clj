(ns exmod.generators
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as gen]

   [exmod.quiz :as quiz]))


(def list-of-string (s/gen (s/coll-of string? :min-count 1 :distinct true)))

(def array-and-two-cursors
  (gen/bind
   list-of-string
   (fn [l] (gen/tuple
            (gen/return l)
            (gen/choose 0 (count l))
            (gen/one-of [(gen/choose 0 (count l)) (gen/return nil)])))))

(def quiz-gen
  (gen/fmap
   (fn [[arr cur score]] {::quiz/questions arr ::quiz/current cur ::quiz/score score})
   array-and-two-cursors))
