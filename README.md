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
  (:require [hum.core :as hum])

(def ctx (hum/create-context))
(def vco (hum/create-osc ctx :sawtooth))
(def vcf (hum/create-biquad-filter ctx))
(def output (hum/create-gain ctx))

; connect the VCO to the VCF and on to the output gain node
(hum/connect vco vcf output)

(hum/start-osc vco)

(hum/connect-output output)

(hum/note-on output vco 440)
```

## What now? / Contributing

If you are using `hum` in your app, I'd love to hear about it. If you want to suggest functionality, then please submit an Issue, or even better, a Pull Request! I'd like to build up an API of functions that people find useful for making music and software instruments, but I'll need your help to get there. Thanks in advance!

## License

Copyright © 2013 Matt Gauger.

Distributed under the Eclipse Public License version 1.0 or (at
your option) any later version.
