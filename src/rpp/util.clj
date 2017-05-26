(ns rpp.util)

(defn remove-one
  "Returns vector v without the first instance of item."
  [item v]
  (let [[without-item with-item] (split-with #(not (= item %)) v)]
    (vec (concat without-item (rest with-item)))))

(defn has [player inventory-item]
  (some #{inventory-item} (:inventory player)))

(defn beats
  [play1 play2]
  (case play1
    :rock (= play2 :scissors)
    :paper (= play2 :rock)
    :scissors (= play2 :paper)))
