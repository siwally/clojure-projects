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

(defn row-idxs
  []
  (->> (range grid-el-count)
       (partition grid-width)))

(defn col-idxs
  []
  (->> (range grid-el-count)
       (sort-by #(mod % grid-width))
       (partition grid-width)))

(defn diag-idxs-from-top-l
  []
  (->> (range grid-el-count)
       (partition-all (inc grid-width))
       (map first)
       (vector)))

(defn diag-idxs-from-top-r
  []
  (->> (range (dec grid-width) grid-el-count)
       (partition (dec grid-width))
       (map first)
       (vector)))

(defn all-idxs
  []
  (->> (concat (diag-idxs-from-top-l) (diag-idxs-from-top-r))
       (concat (col-idxs))
       (concat (row-idxs))))

(defn winning-line?
  [idxs grid plyr]
  (every? #(= plyr (nth grid %)) idxs))

(defn winner?
  [grid plyr]
  (->> (all-idxs)
       (map #(winning-line? % grid plyr))
       (some true?)))
