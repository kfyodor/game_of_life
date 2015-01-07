(ns conways-game-of-life.core
  [:require [quil.core :as q]
            [conways-game-of-life.life :as life]])

(def grid-width   5)
(def board-width  150)
(def board-height 100)
(def cell-color   {true  [10 184 55]
                   false [0 0 0]})

(def padding 20)


(defn scale
  [coord]
  (+ (int (/ padding 2))
     (* grid-width coord)))

(defn setup []
  (q/frame-rate 15)
  (q/background 0x0))

(defn draw-state
  [life-state]
  (q/background 0x0)
  (q/no-stroke)

  (let [board (@life-state :state)]
    (doseq [[x y] (keys board)]
        (apply q/fill (cell-color true))
        (q/rect (scale x) (scale y) grid-width grid-width))))

(defn next-generation!
  [life-state]
  (swap! life-state update-in [:state] life/step)
  (swap! life-state update-in [:generation] inc))


(defn run!
  ([]
   (run! (life/random-cells board-width
                            board-height)))
  ([cells]
   (let [life-state (atom {:generation 1
                           :state      (life/init-state board-width
                                                        board-height
                                                        cells)})]
     (q/defsketch game-of-life
       :title "Conway's Game Of Life"
       :setup    setup
       :draw     (fn []
                   (draw-state life-state)
                   (next-generation! life-state))
       :features [:keep-on-top]
       :size     (map (comp (partial + padding)
                            (partial * grid-width))
                      [board-width
                       board-height])))))
