(ns room-service.config
  (:require [room-service.utils :refer [modified-since-days delete move]]))

(def config
  ["C:\\Users\\Paul\\Downloads"
   [[#".*.(deb|xz|tar.gz|zip|tgz|gz|exe|msi)"
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
         (let [dest (str "C:\\Users\\Paul\\Music" (.getName file))]
           (move file dest))))]
    
    [#".*.(csv|doc|docx|gem|vcs|ppt|js|rb|xlsx|xml)"
     (fn [file]
       (when-not (modified-since-days 7 file)
         (delete file)))]
    
    [#".*.(mov|mp4|m4v|ogv|webm|avi|mkv)"
     (fn [file]
       (let [dest (str "C:\\Users\\Paul\\Videos" (.getName file))]
         (move file dest)))]
    
    [#".*.(epub|mobi|pdf)"
     (fn [file]
       (let [dest (str "C:\\Users\\Paul\\Documents" (.getName file))]
         (move file dest)))]]])
