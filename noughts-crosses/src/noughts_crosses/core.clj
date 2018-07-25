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
(defn make-move
  [moves move]
  (if (and (free? moves move) (in-range? move))
    (conj moves move)
    (throw (IllegalArgumentException. "Move must be in the range 0..2 and refer to a free slot on the grid"))))

(defn play-fn
  []
  (let [moves (atom [])]
  (fn [move] (do (swap! moves make-move move)))))
