(ns hum.core)

(defn create-osc
  ([ctx]
     (.createOscillator ctx))
  ([ctx type]
     (let [osc (.createOscillator ctx)
           osc-type (name type)]
       (set! (.-type osc) osc-type)
       osc)))

(defn set-value 
  "Set nodes attribute."
  [node param value]
  (aset node (name param) "value" value))

(defn set-value-at 
  "Set nodes param value at time."
  [node param value time]
  (.setValueAtTime (aget node (name param)) value time))

(defn linear-fade 
  "Fade to nodes param value by time linearly."
  [node param value time]
  (.linearRampToValueAtTime (aget node (name param)) value time))

(defn exponential-fade
  "Fade to nodes param value by time exponentially."
  [node param value time]
  (.exponentialRampToValueAtTime (aget node (name param)) value time))

(defn set-gain-to [channel val]
  (set! (.-value (.-gain channel)) val))

; envelope stuff

(defn trigger-env
  "Triggers envelopes in the form of [[curve-function value time]...]"
  ([node param envelope time] 
    (doall (map (fn [[f v t]] (apply f [node param v (+ time t)])) 
                envelope))))

(defn env-of-type
  "Creates an envelope that transitions between times with func.
  First time is set as start value hence times is shorter. 
  All others interpolated between."

  [func values times]
  (cons [set-value-at (first values) 0]
        (map (fn [value time] [func value time]) 
             (rest values) times)))

(defn linear-env      
  "Creates a linear envelope."
  [values times] (env-of-type linear-fade values times))

(defn exponential-env 
  "Creates an exponential envelope."
  [values times] (env-of-type exponential-fade values times))

(defn step-env
  "Creates an envelope with stepped values."
  [values times] (env-of-type set-value-at values times))

(defn create-gain
  ([ctx]
     (create-gain ctx 0))
  ([ctx level]
     (let [gain (.createGain ctx)]
       (set-gain-to gain level)
       gain)))

(defn set-buffer-to [buffer-src buffer]
  (set! (.-buffer buffer-src) buffer))

(defn create-buffer-source
  ([ctx] (.createBufferSource ctx))
  ([ctx buffer]
   (let [buffer-src (create-buffer-source ctx)]
     (set-buffer-to buffer-src buffer)
     buffer-src)))

(defn create-delay [ctx]
  (.createDelay ctx))

(defn create-convolver [ctx]
  (.createConvolver ctx))

(defn create-dynamics-compressor [ctx]
  (.createDynamicsCompressor ctx))

(defn create-biquad-filter [ctx]
  (let [filter (.createBiquadFilter ctx)]
    filter))

(defn connect [& nodes]
  (doall
    (map (fn [[a b]] (.connect a b))
         (partition 2 1 nodes))))

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
