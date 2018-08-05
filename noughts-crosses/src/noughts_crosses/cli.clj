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
  (let [new-grid (read-and-apply-move grid plyr)]
   (cond
    (game/winner? new-grid plyr) (println (format "%s wins!" plyr))
    (game/game-ended? new-grid) (println "Game is drawn: no heroes, no zeroes.")
    :else (recur new-grid (if (= :X plyr) :O :X)))))
