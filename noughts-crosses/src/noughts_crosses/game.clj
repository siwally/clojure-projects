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

(defn els
  [grid idxs]
  (map #(nth grid %) idxs))

(defn rows
  [grid]
  (->> (range grid-el-count)
       (partition grid-width)
       (map #(els grid %))))

(defn cols
  [grid]
  (->> (range grid-el-count)
       (sort-by #(mod % grid-width))
       (partition grid-width)
       (map #(els grid %))))

(defn diag-from-top-l
  [grid]
  (->> (range grid-el-count)
       (partition-all (inc grid-width))
       (map first)
       (vector)
       (map #(els grid %))))

(defn diag-from-top-r
  [grid]
  (->> (range (dec grid-width) grid-el-count)
       (partition (dec grid-width))
       (map first)
       (vector)
       (map #(els grid %))))

(defn all-lines
  [grid]
  (->> (concat (diag-from-top-l grid) (diag-from-top-r grid))
       (concat (cols grid))
       (concat (rows grid))))

(defn winning-line?
  [idxs grid plyr]
  (every? #(= plyr %) idxs))

(defn winner?
  [grid plyr]
  (->> (all-lines grid)
       (map #(winning-line? % grid plyr))
       (some true?)))
