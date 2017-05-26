;; gorilla-repl.fileformat = 1

;; @@
(ns rpp.worksheet
  (:require [rpp.util :as u]
            [rpp.player-functions :as pf]
            [rpp.play :as p]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(:report (p/play 100
                    {:name "Randy" :function pf/random-player-function}
                    {:name "Rando" :function pf/random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Randy; Randy: 42; Rando: 37&quot;</span>","value":"\"Winner: Randy; Randy: 42; Rando: 37\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Randy" :function pf/random-player-function}
                 {:name "Rocky" :function pf/prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Rocky; Randy: 35; Rocky: 43&quot;</span>","value":"\"Winner: Rocky; Randy: 35; Rocky: 43\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Rocky" :function pf/prefer-rock-player-function}
                 {:name "Roxy" :function pf/prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Nobody; Rocky: 0; Roxy: 0&quot;</span>","value":"\"Winner: Nobody; Rocky: 0; Roxy: 0\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Margie" :function pf/majority-player-function}
                 {:name "Randy" :function pf/random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Margie; Margie: 46; Randy: 37&quot;</span>","value":"\"Winner: Margie; Margie: 46; Randy: 37\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Minnie" :function pf/minority-player-function}
                 {:name "Randy" :function pf/random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Randy; Minnie: 36; Randy: 37&quot;</span>","value":"\"Winner: Randy; Minnie: 36; Randy: 37\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Minnie" :function pf/minority-player-function}
                 {:name "Margie" :function pf/majority-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Minnie; Minnie: 51; Margie: 49&quot;</span>","value":"\"Winner: Minnie; Minnie: 51; Margie: 49\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Minnie" :function pf/minority-player-function}
                 {:name "Rocky" :function pf/prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Minnie; Minnie: 35; Rocky: 34&quot;</span>","value":"\"Winner: Minnie; Minnie: 35; Rocky: 34\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Donnie" :function pf/dominate-absent-player-function}
                 {:name "Randy" :function pf/random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 57; Randy: 20&quot;</span>","value":"\"Winner: Donnie; Donnie: 57; Randy: 20\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Donnie" :function pf/dominate-absent-player-function}
                 {:name "Rocky" :function pf/prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Rocky; Donnie: 0; Rocky: 2&quot;</span>","value":"\"Winner: Rocky; Donnie: 0; Rocky: 2\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Donnie" :function pf/dominate-absent-player-function}
                 {:name "Minnie" :function pf/minority-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Minnie; Donnie: 0; Minnie: 2&quot;</span>","value":"\"Winner: Minnie; Donnie: 0; Minnie: 2\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Rocky" :function pf/prefer-rock-player-function}
                 {:name "Suzy" :function pf/prefer-scissors-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Nobody; Rocky: 42; Suzy: 42&quot;</span>","value":"\"Winner: Nobody; Rocky: 42; Suzy: 42\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Echo" :function pf/echo-player-function}
                 {:name "Randy" :function pf/random-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Nobody; Echo: 41; Randy: 41&quot;</span>","value":"\"Winner: Nobody; Echo: 41; Randy: 41\""}
;; <=

;; @@
(p/tournament 10 100 
              {:name "Echo" :function pf/echo-player-function}
              {:name "Randy" :function pf/random-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Randy&quot;</span>","value":"\"Randy\""},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"}],"value":"[\"Randy\" 3]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Echo&quot;</span>","value":"\"Echo\""},{"type":"html","content":"<span class='clj-long'>6</span>","value":"6"}],"value":"[\"Echo\" 6]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Nobody&quot;</span>","value":"\"Nobody\""},{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"}],"value":"[\"Nobody\" 1]"}],"value":"{\"Randy\" 3, \"Echo\" 6, \"Nobody\" 1}"}
;; <=

;; @@
(p/tournament 10 100 
              {:name "Echo" :function pf/echo-player-function}
              {:name "Minnie" :function pf/minority-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Echo&quot;</span>","value":"\"Echo\""},{"type":"html","content":"<span class='clj-long'>6</span>","value":"6"}],"value":"[\"Echo\" 6]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Minnie&quot;</span>","value":"\"Minnie\""},{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"[\"Minnie\" 4]"}],"value":"{\"Echo\" 6, \"Minnie\" 4}"}
;; <=

;; @@
(p/tournament 10 100 
              {:name "Echo" :function pf/echo-player-function}
              {:name "Donnie" :function pf/dominate-absent-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"}],"value":"[\"Donnie\" 10]"}],"value":"{\"Donnie\" 10}"}
;; <=

;; @@
(p/tournament 10 100 
              {:name "Donnie" :function pf/dominate-absent-player-function}
              {:name "Danny" :function pf/dominate-absent-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Danny&quot;</span>","value":"\"Danny\""},{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"[\"Danny\" 4]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>6</span>","value":"6"}],"value":"[\"Donnie\" 6]"}],"value":"{\"Danny\" 4, \"Donnie\" 6}"}
;; <=

;; @@
(p/tournament 10 100 
              {:name "Donnie" :function pf/dominate-absent-player-function}
              {:name "Rocky" :function pf/prefer-rock-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>7</span>","value":"7"}],"value":"[\"Donnie\" 7]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Rocky&quot;</span>","value":"\"Rocky\""},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"}],"value":"[\"Rocky\" 3]"}],"value":"{\"Donnie\" 7, \"Rocky\" 3}"}
;; <=

;; @@
(:report (p/play 100
                 {:name "Donnie" :function pf/dominate-absent-player-function}
                 {:name "Rocky" :function pf/prefer-rock-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 10; Rocky: 3&quot;</span>","value":"\"Winner: Donnie; Donnie: 10; Rocky: 3\""}
;; <=

;; @@
(:report (p/play 100
                 {:name "Donnie" :function pf/dominate-absent-player-function}
                 {:name "Echo" :function pf/echo-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 64; Echo: 17&quot;</span>","value":"\"Winner: Donnie; Donnie: 64; Echo: 17\""}
;; <=

;; @@
(p/tournament 100 100
              {:name "Donnie" :function pf/dominate-absent-player-function}
              {:name "Minnie" :function pf/minority-player-function})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>52</span>","value":"52"}],"value":"[\"Donnie\" 52]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Minnie&quot;</span>","value":"\"Minnie\""},{"type":"html","content":"<span class='clj-long'>48</span>","value":"48"}],"value":"[\"Minnie\" 48]"}],"value":"{\"Donnie\" 52, \"Minnie\" 48}"}
;; <=

;; @@
(p/tournament 100 100
              {:name "Donnie" :function pf/dominate-absent-player-function}
              {:name "Smarty" :function pf/doesnt-play-what-beats-what-opponent-doesnt-have-function})


;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>100</span>","value":"100"}],"value":"[\"Donnie\" 100]"}],"value":"{\"Donnie\" 100}"}
;; <=

;; @@
(p/tournament 100 100
              {:name "Donnie" :function pf/dominate-absent-player-function}
              {:name "Betty" :function pf/beat-dominate-absent-player-function})

;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Donnie&quot;</span>","value":"\"Donnie\""},{"type":"html","content":"<span class='clj-long'>100</span>","value":"100"}],"value":"[\"Donnie\" 100]"}],"value":"{\"Donnie\" 100}"}
;; <=

;; @@
(:report (p/play 100
                 {:name "Donnie" :function pf/dominate-absent-player-function}
                 {:name "Betty" :function pf/beat-dominate-absent-player-function}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Winner: Donnie; Donnie: 55; Betty: 24&quot;</span>","value":"\"Winner: Donnie; Donnie: 55; Betty: 24\""}
;; <=

;; @@

;; @@
