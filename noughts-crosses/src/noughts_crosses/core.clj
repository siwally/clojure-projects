(ns noughts-crosses.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello World!"))

(defn free?
  [moves pos]
  (not (.contains moves pos)))

(defn in-range?
  [pos]
  (<= 0 pos 2))

;; play function takes a move, validates it and returns an updates list of moves
(defn play
  [moves move]
  (if (and (free? moves move) (in-range? move))
    (conj moves move)
    (throw (IllegalArgumentException. "Illegal move"))))
