(ns room-service.utils
  (:require [clj-time.coerce :as coerce]
            [clojure.java.io :as io]
            [clj-time.core :as cljtime]))

(def dry-run true)

(defn modified-since [time file]
  (cljtime/after? (coerce/from-long (.lastModified file)) time))

(defn delete [file]
  (if (true? dry-run)
    (println (str "delete " (.getName file)))
    (do
      (println (str "deleting " (.getName file)))
      (.delete file))))

(defn move [file dest]
  (if (true? dry-run)
    (println (str "move " (.getName file) " to " dest))
    (do
      (println (str "moving " (.getName file) " to " dest))
      (.renameTo file (io/file dest)))))

(defn modified-since-days [days file]
  (modified-since (cljtime/minus (cljtime/now) (cljtime/days days)) file))
