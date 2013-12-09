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

(defn create-gain
  ([ctx]
     (create-gain ctx 0))
  ([ctx level]
     (let [gain (.createGain ctx)]
       (set-gain-to gain level)
       gain)))

(defn create-biquad-filter [ctx]
  (let [filter (.createBiquadFilter ctx)]
    filter))

(defn connect [from to]
  (.connect from to)
  from)

(defn ctx-for [audio-node]
  (.-context audio-node))

(defn connect-output [output]
  (.connect output (.-destination (ctx-for output)))
  output)

(defn freq [filter]
  (.-frequency filter)
  filter)

(defn curr-time [ctx]
  (.-currentTime ctx))

(defn start-osc [osc]
  (.start osc (curr-time (ctx-for osc))))

(defn note-on
  [output osc freq & {:keys [time ramp-time]
                      :or {time (curr-time (ctx-for osc))
                           ramp-time 0.1}}]
  (.setValueAtTime (.-frequency osc) freq time)
  (.linearRampToValueAtTime (.-gain output) 1.0 (+ time ramp-time)))

(defn note-off
  [output & {:keys [time ramp-time]
             :or {time (curr-time (ctx-for output))
                  ramp-time 0.1}}]
  (.linearRampToValueAtTime (.-gain output) 0.0 (+ time ramp-time)))

(defn create-context []
  (let [constructor (or js/window.AudioContext
                        js/window.webkitAudioContext)]
    (constructor.)))
