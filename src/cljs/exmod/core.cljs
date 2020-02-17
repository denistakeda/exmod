(ns exmod.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [exmod.events :as events]
   [exmod.views :as views]
   [exmod.config :as config]
   [orchestra-cljs.spec.test :as st]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")
    (st/instrument)
    (println "instrumentation enabled")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
