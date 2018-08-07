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

(defn grid-full?
  [grid]
  (every? #(not= :- %) grid))

(defn winning-line?
  [idxs grid plyr]
  (every? #(= plyr (nth grid %)) idxs))

(defn winning-row?
  [grid plyr]
  (some true?
        (map #(winning-line? % grid plyr) (partition-all grid-width (range grid-el-count)))))

(defn winning-col?
  [grid plyr]
  (let [col-idxs (fn [col] (filter (fn [idx] (= col (mod idx grid-width))) (range grid-el-count)))]
    (some true?
          (map #(winning-line? (col-idxs %) grid plyr) (range grid-width)))))

(defn winning-diag?
  [grid plyr]
  (let [idxs-from-top-l (map first (partition-all (inc grid-width) (range grid-el-count)))
        idxs-from-top-r (map first (partition (dec grid-width) (range (dec grid-width) grid-el-count)))]
    (or (winning-line? idxs-from-top-l grid plyr)
        (winning-line? idxs-from-top-r grid plyr))))

(defn winner?
  [grid plyr]
  (or (winning-row? grid plyr)
      (winning-col? grid plyr)
      (winning-diag? grid plyr)))

; TODO Potentially simplify winning-col, either with partioning or group-by for col mod
; Something like (map #(map first (partition-all grid-width (range % grid-el-count))) (range grid-width))?
