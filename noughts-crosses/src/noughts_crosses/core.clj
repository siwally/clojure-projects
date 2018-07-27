(ns noughts-crosses.core
  (:gen-class))

(defn initial-grid
  []
  [:- :- :-])

(defn move
  [grid pos plyr]
  {:pre [(<= 0 pos 2), (= :- (nth grid pos))]}
    (assoc grid pos plyr))

(defn winner?
  [grid plyr]
  (= (count grid) (count (filter #(= % plyr) grid))))

(defn play-fn
  []
  (let [grid (atom (initial-grid))]
    (fn [pos plyr] (do (swap! grid move pos plyr)))))
