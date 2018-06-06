(ns noughts-crosses.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn start-game
  "define postions in use"
  []
  (def moves (atom [false false false]))
  )

(defn ?pos-in-use
  [pos]
  (nth (deref moves) pos)
  )

(defn record-move
  "set postion to true"
  [pos]
  (def moves (atom (assoc (deref moves) pos true)))
  )

(defn move
  "move player"
  [pos, player]
  (if (or (< pos 0) (> pos 2))
     (throw (IllegalArgumentException. "Moves must be between 0 and 2"))
    )

  (if (?pos-in-use pos)
    false                                                   ; error, in use
    (do
      (record-move pos)
      true
   )
)
)