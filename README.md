# Conway's Game of Life

[Conway's Game of Life](http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) implementation in [Clojure](http://clojure.org) using [Quil](https://github.com/quil/quil) for rendering.

![Random initial state](http://zippy.gfycat.com/QuarrelsomeThatBirdofparadise.gif)

## Usage
Type in your repl (`lein repl`)

```clojure
(use 'conways_game_of_life.core)
(run!)
```

To see [Gosper glider gun](http://www.conwaylife.com/wiki/Gosper_glider_gun) in action type:

```clojure
(run! life/glider-gun)
```

![Gosper glider gun](http://zippy.gfycat.com/AgreeableHeavyCob.gif)

## License

Copyright © 2015 Theodore Konukhov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
