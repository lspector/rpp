;; gorilla-repl.fileformat = 1

;; @@
(ns rpp.core
  (:gen-class))

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
            play1 (if (has player1 play1) play1 (rand-nth (:inventory player1)))
            {play2 :play memory2 :memory} ((:function player2) player2)
            play2 (if (has player2 play2) play2 (rand-nth (:inventory player2)))]
        (recur (inc step)
               (assoc player1
                 :inventory (conj (remove-one play1 (:inventory player1)) play2)
                 :score (if (beats play1 play2) 
                          (inc (:score player1))
                          (:score player1))
                 :history (conj (:history player1) [play1 play2])
                 :memory (merge (:memory player1) memory1))
               (assoc player2
                 :inventory (conj (remove-one play2 (:inventory player2)) play1)
                 :score (if (beats play2 play1)
                          (inc (:score player2))
                          (:score player2))
                 :history (conj (:history player2) [play2 play1])
                 :memory (merge (:memory player2) memory2)))))))

(defn tournament
  [games steps-per-game player1 player2]
  (let [results (repeatedly games #(play steps-per-game player1 player2))]
    (frequencies (map :name (map :winner results)))))

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
  {:play (if (and (not (has p :rock))
                  (has p :paper))
           :paper
           (if (and (not (has p :paper))
                    (has p :scissors))
             :scissors
             (if (and (not (has p :scissors))
                      (has p :rock))
               :rock
               (rand-nth (:inventory p)))))})

(defn echo-player-function
  [p]
  {:play (last (last (:history p)))})

; ANYA: Choose random category.

; ANYA: Never play what beats what you have two of, if still have options choose a random category.

; ANYA minority-player-function should beat dominate-absent-player-function... but it actually looks like an even match

;; ANYA random category among possible winners


(defn doesnt-play-what-beats-what-opponent-doesnt-have-function
  [p]
  (let [opponent-doesnt-have (filter #(> (count (filter #{%} (:inventory p))) 
                                         1)
                                     [:rock :paper :scissors])]
    (if (empty? opponent-doesnt-have)
      (rand-nth (:inventory p))
      (let [beats-it (first (filter #(beats % (first opponent-doesnt-have))
                                    [:rock :paper :scissors]))]
        (rand-nth (distinct (filter #(not (= % beats-it)) (:inventory p))))))))

(defn beat-dominate-absent-player-function
  [p]
  (let [inv (:inventory p)
        other {:inventory 
               (remove-one (first inv)
                           (remove-one (second inv)
                                       (remove-one (nth inv 2)
                                                   [:rock :rock 
                                                    :paper :paper 
                                                    :scissors :scissors])))}
        other-play (if (and (not (has other :rock))
                            (has other :paper))
                     :paper
                     (if (and (not (has other :paper))
                              (has other :scissors))
                       :scissors
                       (if (and (not (has other :scissors))
                                (has other :rock))
                         :rock
                         (rand-nth (:inventory other)))))
        ideal (first (filter #(beats % other-play)
                             [:rock :paper :scissors]))]
    (if (has p ideal)
      ideal
      (rand-nth (distinct inv)))))
        
    

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;rpp.core/-main</span>","value":"#'rpp.core/-main"}
;; <=

;; @@
(:report (play 100
               {:name "Randy" :function random-player-function}
               {:name "Rando" :function random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Randy; Randy: 38; Rando: 37&quot;</span>","value":"\"Winner: Randy; Randy: 38; Rando: 37\""}
;; <=

;; @@
(:report (play 100
               {:name "Randy" :function random-player-function}
               {:name "Rocky" :function prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Rocky; Randy: 34; Rocky: 43&quot;</span>","value":"\"Winner: Rocky; Randy: 34; Rocky: 43\""}
;; <=

;; @@
(:report (play 100
               {:name "Rocky" :function prefer-rock-player-function}
               {:name "Roxy" :function prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Nobody; Rocky: 0; Roxy: 0&quot;</span>","value":"\"Winner: Nobody; Rocky: 0; Roxy: 0\""}
;; <=

;; @@
(:report (play 100
               {:name "Margie" :function majority-player-function}
               {:name "Randy" :function random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Nobody; Margie: 42; Randy: 42&quot;</span>","value":"\"Winner: Nobody; Margie: 42; Randy: 42\""}
;; <=

;; @@
(:report (play 100
               {:name "Minnie" :function minority-player-function}
               {:name "Randy" :function random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Randy; Minnie: 33; Randy: 34&quot;</span>","value":"\"Winner: Randy; Minnie: 33; Randy: 34\""}
;; <=

;; @@
(:report (play 100
               {:name "Minnie" :function minority-player-function}
               {:name "Margie" :function majority-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Margie; Minnie: 49; Margie: 50&quot;</span>","value":"\"Winner: Margie; Minnie: 49; Margie: 50\""}
;; <=

;; @@
(:report (play 100
               {:name "Minnie" :function minority-player-function}
               {:name "Rocky" :function prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Minnie; Minnie: 32; Rocky: 30&quot;</span>","value":"\"Winner: Minnie; Minnie: 32; Rocky: 30\""}
;; <=

;; @@
(:report (play 100
               {:name "Donnie" :function dominate-absent-player-function}
               {:name "Randy" :function random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 60; Randy: 21&quot;</span>","value":"\"Winner: Donnie; Donnie: 60; Randy: 21\""}
;; <=

;; @@
(:report (play 100
               {:name "Donnie" :function dominate-absent-player-function}
               {:name "Rocky" :function prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Rocky; Donnie: 1; Rocky: 3&quot;</span>","value":"\"Winner: Rocky; Donnie: 1; Rocky: 3\""}
;; <=

;; @@
(:report (play 100
               {:name "Donnie" :function dominate-absent-player-function}
               {:name "Minnie" :function minority-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 1; Minnie: 0&quot;</span>","value":"\"Winner: Donnie; Donnie: 1; Minnie: 0\""}
;; <=

;; @@
(:report (play 100
               {:name "Rocky" :function prefer-rock-player-function}
               {:name "Suzy" :function prefer-scissors-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Rocky; Rocky: 42; Suzy: 41&quot;</span>","value":"\"Winner: Rocky; Rocky: 42; Suzy: 41\""}
;; <=

;; @@
(:report (play 100
               {:name "Echo" :function echo-player-function}
               {:name "Randy" :function random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Nobody; Echo: 44; Randy: 44&quot;</span>","value":"\"Winner: Nobody; Echo: 44; Randy: 44\""}
;; <=

;; @@
(tournament 10 100 
            {:name "Echo" :function echo-player-function}
            {:name "Randy" :function random-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Nobody&quot;</span>","value":"\"Nobody\""},{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"}],"value":"[\"Nobody\" 2]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Randy&quot;</span>","value":"\"Randy\""},{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"[\"Randy\" 4]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Echo&quot;</span>","value":"\"Echo\""},{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"[\"Echo\" 4]"}],"value":"{\"Nobody\" 2, \"Randy\" 4, \"Echo\" 4}"}
;; <=

;; @@
(tournament 10 100 
            {:name "Echo" :function echo-player-function}
            {:name "Minnie" :function minority-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Echo&quot;</span>","value":"\"Echo\""},{"type":"html","content":"<span class='clj-long'>5</span>","value":"5"}],"value":"[\"Echo\" 5]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Minnie&quot;</span>","value":"\"Minnie\""},{"type":"html","content":"<span class='clj-long'>5</span>","value":"5"}],"value":"[\"Minnie\" 5]"}],"value":"{\"Echo\" 5, \"Minnie\" 5}"}
;; <=

;; @@
(tournament 10 100 
            {:name "Echo" :function echo-player-function}
            {:name "Donnie" :function dominate-absent-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"}],"value":"[\"Donnie\" 10]"}],"value":"{\"Donnie\" 10}"}
;; <=

;; @@
(tournament 10 100 
            {:name "Donnie" :function dominate-absent-player-function}
            {:name "Danny" :function dominate-absent-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Danny&quot;</span>","value":"\"Danny\""},{"type":"html","content":"<span class='clj-long'>7</span>","value":"7"}],"value":"[\"Danny\" 7]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"}],"value":"[\"Donnie\" 3]"}],"value":"{\"Danny\" 7, \"Donnie\" 3}"}
;; <=

;; @@
(tournament 10 100 
            {:name "Donnie" :function dominate-absent-player-function}
            {:name "Rocky" :function prefer-rock-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>8</span>","value":"8"}],"value":"[\"Donnie\" 8]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Rocky&quot;</span>","value":"\"Rocky\""},{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"}],"value":"[\"Rocky\" 2]"}],"value":"{\"Donnie\" 8, \"Rocky\" 2}"}
;; <=

;; @@
(:report (play 100
               {:name "Donnie" :function dominate-absent-player-function}
               {:name "Rocky" :function prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 6; Rocky: 2&quot;</span>","value":"\"Winner: Donnie; Donnie: 6; Rocky: 2\""}
;; <=

;; @@
(:report (play 100
               {:name "Donnie" :function dominate-absent-player-function}
               {:name "Echo" :function echo-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 55; Echo: 19&quot;</span>","value":"\"Winner: Donnie; Donnie: 55; Echo: 19\""}
;; <=

;; @@
(tournament 100 100
            {:name "Donnie" :function dominate-absent-player-function}
            {:name "Minnie" :function minority-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Minnie&quot;</span>","value":"\"Minnie\""},{"type":"html","content":"<span class='clj-long'>54</span>","value":"54"}],"value":"[\"Minnie\" 54]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>46</span>","value":"46"}],"value":"[\"Donnie\" 46]"}],"value":"{\"Minnie\" 54, \"Donnie\" 46}"}
;; <=

;; @@
(tournament 100 100
            {:name "Donnie" :function dominate-absent-player-function}
            {:name "Smarty" :function doesnt-play-what-beats-what-opponent-doesnt-have-function})


;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>100</span>","value":"100"}],"value":"[\"Donnie\" 100]"}],"value":"{\"Donnie\" 100}"}
;; <=

;; @@
(tournament 100 100
            {:name "Donnie" :function dominate-absent-player-function}
            {:name "Betty" :function beat-dominate-absent-player-function})

;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>100</span>","value":"100"}],"value":"[\"Donnie\" 100]"}],"value":"{\"Donnie\" 100}"}
;; <=

;; @@
(:report (play 100
               {:name "Donnie" :function dominate-absent-player-function}
               {:name "Betty" :function beat-dominate-absent-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 56; Betty: 23&quot;</span>","value":"\"Winner: Donnie; Donnie: 56; Betty: 23\""}
;; <=

;; @@

;; @@
