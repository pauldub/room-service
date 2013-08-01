(ns room-service.core
  (:gen-class)
  (:require [room-service.config :refer [config]]
            [clojure.java.io :as io]))

(defn all-files [path]
  (let [files (file-seq (io/file path))]
    (filter (memfn isFile) files)))

(defn clean [path rules]
  (doseq [[rule processor] rules]
    (let [files (for [file (all-files path)
                      :when (re-matches rule (.getName file))]
                  file)]
      (doall (map processor files)))
    rules))

;; From
;; http://stackoverflow.com/questions/4053845/idomatic-way-to-iterate-through-all-pairs-of-a-collection-in-clojure
;; Thanks :)
(defn all-pairs [coll]
  (loop [[x & xs] coll
         result []]
    (if (nil? xs)
      result
      (recur xs (concat result (map #(vector x %) xs))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (doseq [[path rules] (all-pairs config)]
    (clean path rules)))
