(ns exmod.utils)

(defn ?assoc
  "Same as assoc, but skip the assoc if v is nil"
  [m & kvs]
  (->> kvs
       (partition 2)
       (filter second)
       flatten
       (apply assoc m)))

(defn index-in-range?
  "Checks that some index `idx` is in range of some sequence `list`"
  [idx list]
  (>= 0 idx (dec (count list))))
