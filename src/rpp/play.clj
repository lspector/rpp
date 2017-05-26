(ns rpp.play
  (:require [rpp.util :as u]))

;; a player is: 
;;
;; {:name <string>
;;  :inventory <vector of items from #{:rock :paper :scissors}>
;;  :score <integer>
;;  :function <fn -- see below>
;;  :history <vec of [myplay yourplay] pairs that grows at the end>
;;  :memory <map of anything to anything, under player's control>}

;; a player function is called on the player itself and must return
;; {:play <item from #{:rock :paper :scissors}>
;;  :memory <map that will be merged into the player's :memory>}

(defn player
  [name function]
  {:name name
   :inventory [:rock :paper :scissors]
   :score 0
   :function function
   :history []
   :memory {}})

(defn play 
  [steps
   {name1 :name function1 :function}
   {name2 :name function2 :function}]
  (loop [step 0
         player1 (player name1 function1)
         player2 (player name2 function2)]
    (if (>= step steps)
      (let [s1 (:score player1)
            s2 (:score player2)
            winner (if (> s1 s2) 
                     player1
                     (if (> s2 s1)
                       player2
                       {:name "Nobody"}))]
        {:winner winner
         :report (str "Winner: " (:name winner) "; "
                      name1 ": " s1 "; " name2 ": " s2)
         :player1 player1
         :player2 player2})
      (let [{play1 :play memory1 :memory} ((:function player1) player1)
            play1 (if (u/has player1 play1) play1 (rand-nth (:inventory player1)))
            {play2 :play memory2 :memory} ((:function player2) player2)
            play2 (if (u/has player2 play2) play2 (rand-nth (:inventory player2)))]
        (recur (inc step)
               (assoc player1
                 :inventory (conj (u/remove-one play1 (:inventory player1)) play2)
                 :score (if (u/beats play1 play2) 
                          (inc (:score player1))
                          (:score player1))
                 :history (conj (:history player1) [play1 play2])
                 :memory (merge (:memory player1) memory1))
               (assoc player2
                 :inventory (conj (u/remove-one play2 (:inventory player2)) play1)
                 :score (if (u/beats play2 play1)
                          (inc (:score player2))
                          (:score player2))
                 :history (conj (:history player2) [play2 play1])
                 :memory (merge (:memory player2) memory2)))))))

(defn tournament
  [games steps-per-game player1 player2]
  (let [results (repeatedly games #(play steps-per-game player1 player2))]
    (frequencies (map :name (map :winner results)))))

