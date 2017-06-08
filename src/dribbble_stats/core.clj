(ns dribbble-stats.core
  (:require [dribbble-stats.fetchers :as f]))

(defn print-formatted [liker-data]
  (let [likes (first liker-data)
        username (get (last liker-data) "username")]
    (println
      (str "Likes: " likes " username: " username))))

(defn top-likers [identity]
  (->> (f/fetch-user identity)
       f/fetch-followers
       (mapcat f/fetch-shots)
       f/fetch-top-likers
       (map print-formatted)))
