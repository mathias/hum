# hum

A ClojureScript attempt at wrapping some of the HTML5 Web Audio API functions to create audio, synthesizers, mostly noise, and maybe someday music.

## Usage
Add this to your requires in `project.clj`:

```clojure
  [hum "0.2.1"]
```

Here's an example:
```clojure
(ns myapp.core
  (:require [hum.core :as hum])

(def ctx (hum/create-context))
(def vco (hum/create-osc ctx :sawtooth))
(def vcf (hum/create-biquad-filter ctx))
(def output (hum/create-gain ctx))

(hum/connect vco vcf)
(hum/connect vcf output)

(hum/set-gain-to output 0)
(hum/start-osc ctx vco)

(hum/connect output (.-destination ctx))

(hum/note-on ctx output vco 440)
```

## License

Copyright © 2013 Matt Gauger.

Distributed under the Eclipse Public License version 1.0 or (at
your option) any later version.
