(ns conways-game-of-life.core
  [:require [quil.core :as q]
            [conways-game-of-life.life :as life]])

(def grid-width   10)
(def board-width  80)
(def board-height 50)
(def cell-color   {true  [10 184 55]
                   false [0 0 0]})


(defn trans
  [coord]
  (* grid-width coord))

(defn setup []
  (q/frame-rate 25)
  (q/background 0x0))

(defn draw-state
  [life-state]
  (q/fill 0x0)
  (q/background 0x0)
  (q/no-stroke)

  (doseq [[x y alive] (:board @life-state)]
    (apply q/fill (cell-color alive))
    (q/rect (trans x) (trans y) grid-width grid-width)))

(defn next-generation!
  [life-state]
  (swap! life-state update-in [:board] life/step)
  (swap! life-state update-in [:generation] inc))


(defn run!
  ([]
   (run! (life/random-cells board-width
                            board-height)))
  ([cells]
   (let [life-state (atom {:generation 1
                           :board      (life/init-board board-width
                                                        board-height
                                                        cells)})]
     (q/defsketch game-of-life
       :title "Conway's Game Of Life"
       :setup    setup
       :draw     (fn []
                   (draw-state life-state)
                   (next-generation! life-state))
       :features [:keep-on-top]
       :size     (map (partial * grid-width) [board-width
                                              board-height])))))
