(ns noughts-crosses.cli
  (:gen-class)
  (:require [noughts-crosses.game :as game]))

(defn print-grid
  ([grid] (print-grid grid 0))
  ([grid idx]
   (when (seq grid)
     (when (= 0 (mod idx game/grid-width)) (newline)) ; print newline if new row
     (print (format "(%d)%s " idx (first grid))) ; print X, O or - at this position
     (recur (rest grid) (inc idx)))))

; TODO Handle invalid input and retry
(defn read-and-apply-move
  [grid plyr]
  (println (format "\n\nYour move, %s" plyr))
  (game/move grid (Integer/parseInt (read-line)) plyr))

(defn play
  [grid plyr]
  (print-grid grid)
  (cond
    (game/winner? grid plyr) (println (format "\n\n%s wins!" plyr))
    (game/grid-full? grid)   (println "\n\nGame is drawn: no heroes, no zeroes.")
    :else (let [next-plyr (if (= :X plyr) :O :X)
                new-grid (read-and-apply-move grid next-plyr)]
            (recur new-grid next-plyr))))

(defn -main
  []
  (play game/initial-grid :O))
