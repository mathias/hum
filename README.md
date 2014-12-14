# hum

A ClojureScript library wrapping some of the HTML5 Web Audio API functions to create audio, synthesizers, and maybe someday music.

## Browser support

Hum is now known to work on both Webkit and Firefox browsers.

## Demo

Check out a simple synth demo and code that was used to make it: [http://blog.mattgauger.com/hum](http://blog.mattgauger.com/hum)

## Usage
Add this to your requires in `project.clj`:

```clojure
  [hum "0.4.0"]
```

Here's an example:
```clojure
(ns myapp.core
  (:require [hum.core :as hum]
  	    [hum.envelope :as envelope])

; create a WebAudio contet
(defonce ctx (hum/create-context))

; create an oscilator, filter and amp
(def vco (hum/create-osc ctx :sawtooth))
(def vcf (hum/create-biquad-filter ctx))
(def amp (hum/create-gain ctx))

; connect them together in series
(hum/connect vco vcf amp)

; start the oscilator
(hum/start-osc vco)

; connect the amp to speakers
(hum/connect-output amp)

; move filter frequency between values using an exponential envelope.
(envelope/trigger vcf :frequency 
                  (envelope/exponential [100 10000 1000 5000 100] [4 3 2 1])
                  (hum/curr-time ctx))

; set envelope for amp
(hum/set-value-at amp :gain 1 (+ (hum/curr-time ctx) 0))
(hum/linear-fade amp :gain 0 (+ (hum/curr-time ctx) 10))
```

## What now? / Contributing

If you are using `hum` in your app, I'd love to hear about it. If you want to suggest functionality, then please submit an Issue, or even better, a Pull Request! I'd like to build up an API of functions that people find useful for making music and software instruments, but I'll need your help to get there. Thanks in advance!

## License

Copyright © 2013 Matt Gauger.

Distributed under the Eclipse Public License version 1.0 or (at
your option) any later version.
