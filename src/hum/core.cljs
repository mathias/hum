(ns hum.core)

(defn get-osc-type [osc type]
  (condp = type
    :sawtooth (.-SAWTOOTH osc)
    :sine (.-SINE osc)
    :square (.-SQUARE osc)
    :triangle (.-TRIANGLE osc)))

(defn create-osc
  ([ctx]
     (.createOscillator ctx))
  ([ctx type]
     (let [osc (.createOscillator ctx)
           osc-type (get-osc-type osc type)]
       (set! (.-type osc) osc-type)
       osc)))

(defn set-gain-to [channel val]
  (set! (.-value (.-gain channel)) val))

(defn create-gain [ctx & level]
  (let [gain (.createGain ctx)
        level (and level 0)]
    (set-gain-to gain level)
    gain))

(defn create-biquad-filter [ctx]
  (let [filter (.createBiquadFilter ctx)]
    filter))

(defn connect [from to]
  (.connect from to)
  from)

(defn connect-output [output]
  (.connect output (.-destination (ctx-for output)))
  output)

(defn freq [filter]
  (.-frequency filter)
  filter)

(defn curr-time [ctx]
  (.-currentTime ctx))

(defn ctx-for [audio-node]
  (.context audio-node))

(defn start-osc [osc]
  (.start osc (curr-time (ctx-for osc))))

(defn note-on [output osc freq & time]
  (let [time (or time (curr-time (ctx-for osc)))]
    (.setValueAtTime (.-frequency osc) freq time)
    (.linearRampToValueAtTime (.-gain output) 1.0 (+ time 0.1))))

(defn note-off [output & time]
  (let [time (or time (curr-time (ctx-for osc)))]
    (.linearRampToValueAtTime (.-gain output) 0.0 (+ time 0.1))))

(defn create-context []
  (let [constructor (or js/window.AudioContext
                        js/window.webkitAudioContext)]
    (constructor.)))
