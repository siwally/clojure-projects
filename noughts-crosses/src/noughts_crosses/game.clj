(ns noughts-crosses.game
  (:gen-class))

(def grid-width  3)
(def grid-el-count (* grid-width grid-width))

(defn initial-grid
  []
  [:- :- :-   ; 0, 1, 2
   :- :- :-   ; 3, 4, 5
   :- :- :-]) ; 6, 7, 8

(defn move
  [grid pos plyr]
  {:pre [(< -1 pos grid-el-count), (= :- (nth grid pos))]}
  (assoc grid pos plyr))

(defn game-ended?
  [grid]
  (= 0 (count (filter #(= :- %) grid))))

(defn winning-line?
  [idxs grid plyr]
  (every? #(= plyr (nth grid %)) idxs))

(defn winning-row?
  [grid plyr]
  (let [rows     (range 0 grid-width)
        row-idxs #(range (* % grid-width) (* (inc %) grid-width))]
    (some true?
          (map #(winning-line? (row-idxs %) grid plyr) rows))))

(defn winning-col?
  [grid plyr]
  (let [cols     (range 0 grid-width)
        col-idxs #(filter (fn [i] (= % (mod i grid-width))) (range grid-el-count))]
    (some true?
          (map #(winning-line? (col-idxs %) grid plyr) cols))))

(defn winning-diag?
  [grid plyr]
  (let [idxs-from-top-l (map #(+ % (* % grid-width)) (range grid-width))
        idxs-from-top-r (map #(+ (- (dec grid-width) %) (* % grid-width)) (range grid-width))]
    (or (winning-line? idxs-from-top-l grid plyr)
        (winning-line? idxs-from-top-r grid plyr))))

(defn winner?
  [grid plyr]
  (or (winning-row? grid plyr)
      (winning-col? grid plyr)
      (winning-diag? grid plyr)))
