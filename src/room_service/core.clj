(ns room-service.core
  (:gen-class)
  (:require [clj-time.core :only [after? now minus days]]
            [clj-time.coerce :only [from-long]]))

(defn modified-since [time file]
  (clj-time.core/after? (clj-time.coerce/from-long (.lastModified file)) time))

(def dry-run true)

(defn delete [file]
  (if (= dry-run false)
    (println (str "delete " (.getName file)))
    (do
      (println (str "deleting " (.getName file)))
      (.delete file))))

(defn move [file dest]
  (if dry-run
    (println (str "move " (.getName file) " to " dest))
    (do
      (println (str "moving " (.getName file) " to " dest))
      (.renameTo dest))))

(defn modified-since-days [days file]
  (modified-since (clj-time.core/minus (clj-time.core/now) (clj-time.core/days days)) file))

(def rules
  [[#".*.(deb|xz|tar.gz|zip|tgz|gz)"
    (fn [file]
      (when-not (modified-since-days 7 file)
        (delete file)))]

   [#".*.iso"
    (fn [file]
      (when-not (modified-since-days 7 file)
        (delete file)))]
   
   [#".*.(mp3|ogg)"
    (fn [file]
      (when-not (modified-since-days 7 file)
        (println (.getPath file))))]
   
   [#".*.(csv|doc|docx|gem|vcs|ppt|js|rb|xlsx|xml)"
    (fn [file]
      (when-not (modified-since-days 7 file)
        (delete file)))]
   
   [#".*.(mov|mp4|m4v|ogv|webm|avi|mkv)"
    (fn [file]
      (let [dest  (str "/home/paul/videos/downloaded/" (.getName file))]
        (move file dest)))]
   
   [#".*.(epub|mobi|pdf)"
    (fn [file]
      (let [dest  (str "/home/paul/docs/downloaded/" (.getName file))]
        (move file dest)))]])

(defn all-files [path]
  (let [files (file-seq (clojure.java.io/file path))]
    (filter (memfn isFile) files)))

(defn clean [path]
  (doseq [[rule processor] rules]
    (let [files (for [file (all-files path)
                      :when (re-matches rule (.getName file))]
                  file)]
      (doall (map processor files)))
    rules))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (clean "/home/paul/downloads"))
