# hum

A ClojureScript attempt at wrapping some of the HTML5 Web Audio API functions to create audio, synthesizers, mostly noise, and maybe someday music.

## Usage

```clojure
(ns myapp.core
  (:require [hum.core :as hum])

(def ctx (hum/create-context))
(def vco (hum/create-osc ctx))
(def lfo (hum/create-osc ctx))
(def lfo-gain (hum/create-gain ctx))
(def vcf (hum/create-biquad-filter ctx))
(def output (hum/create-gain ctx))

(hum/connect vco vcf)
(hum/connect vcf output)
(hum/connect lfo lfo-gain)

(set! (.-value (.-gain output)) 0)
(set! (.-type vco) (.-SAWTOOTH vco))
(set! (.-type lfo) (.-SAWTOOTH lfo))

(hum/start-osc ctx vco)
(hum/start-osc ctx lfo)

(hum/connect output (.-destination ctx))

(hum/note-on ctx output vco 400)
```

## License

Copyright © 2013 Matt Gauger.

Distributed under the Eclipse Public License version 1.0 or (at
your option) any later version.
