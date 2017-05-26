(ns rpp.player-functions
  (:require [rpp.util :as u]))

(defn random-player-function
  [p] 
  {:play (rand-nth (:inventory p))})

(defn prefer-rock-player-function
  [p]
  {:play (if (some #{:rock} (:inventory p)) :rock (rand-nth (:inventory p)))})


(defn prefer-scissors-player-function
  [p]
  {:play (if (some #{:scissors} (:inventory p)) :scissors (rand-nth (:inventory p)))})

(defn majority-player-function
  [p]
  {:play (->> (:inventory p)
              (frequencies)
              (vec)
              (shuffle)
              (sort-by second)
              (last)
              (first))})

(defn minority-player-function
  [p]
  {:play (->> (:inventory p)
              (frequencies)
              (vec)
              (shuffle)
              (sort-by second)
              (first)
              (first))})

(defn dominate-absent-player-function
  [p]
  {:play (if (and (not (u/has p :rock))
                  (u/has p :paper))
           :paper
           (if (and (not (u/has p :paper))
                    (u/has p :scissors))
             :scissors
             (if (and (not (u/has p :scissors))
                      (u/has p :rock))
               :rock
               (rand-nth (:inventory p)))))})

(defn echo-player-function
  [p]
  {:play (last (last (:history p)))})

(defn doesnt-play-what-beats-what-opponent-doesnt-have-function
  [p]
  (let [opponent-doesnt-have (filter #(> (count (filter #{%} (:inventory p))) 
                                         1)
                                     [:rock :paper :scissors])]
    (if (empty? opponent-doesnt-have)
      (rand-nth (:inventory p))
      (let [beats-it (first (filter #(u/beats % (first opponent-doesnt-have))
                                    [:rock :paper :scissors]))]
        (rand-nth (distinct (filter #(not (= % beats-it)) (:inventory p))))))))

(defn beat-dominate-absent-player-function
  [p]
  (let [inv (:inventory p)
        other {:inventory 
               (u/remove-one (first inv)
                             (u/remove-one (second inv)
                                           (u/remove-one (nth inv 2)
                                                         [:rock :rock 
                                                          :paper :paper 
                                                          :scissors :scissors])))}
        other-play (if (and (not (u/has other :rock))
                            (u/has other :paper))
                     :paper
                     (if (and (not (u/has other :paper))
                              (u/has other :scissors))
                       :scissors
                       (if (and (not (u/has other :scissors))
                                (u/has other :rock))
                         :rock
                         (rand-nth (:inventory other)))))
        ideal (first (filter #(u/beats % other-play)
                             [:rock :paper :scissors]))]
    (if (u/has p ideal)
      ideal
      (rand-nth (distinct inv)))))

