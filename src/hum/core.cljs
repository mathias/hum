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

(defn create-gain [ctx]
  (let [gain (.createGain ctx)]
    gain))

(defn create-biquad-filter [ctx]
  (let [filter (.createBiquadFilter ctx)]
    filter))

(defn connect [from to]
  (.connect from to)
  from)

(defn freq [filter]
  (.-frequency filter)
  filter)

(defn curr-time [ctx]
  (.-currentTime ctx))

(defn start-osc [ctx osc]
  (.start osc (curr-time ctx)))

(defn setGainTo [output val]
  (set! (.-value (.-gain output)) val))

(defn note-on [ctx output osc freq & time]
  (let [time (or time (curr-time ctx))]
    (.setValueAtTime (.-frequency osc) freq time)
    (.linearRampToValueAtTime (.-gain output) 1.0 (+ time 0.1))))

(defn note-off [ctx output & time]
  (let [time (or time (curr-time ctx))]
    (.linearRampToValueAtTime (.-gain output) 0.0 (+ time 0.1))))

(defn create-context []
  (js/webkitAudioContext.))
