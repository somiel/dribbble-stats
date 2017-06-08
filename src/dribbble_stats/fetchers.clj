(ns dribbble-stats.fetchers
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [dribbble-stats.config :as cfg]
            [dribbble-stats.utils :as u]
            [clojure.string :as str :refer [blank?]]))

(defn- fetch [url]
  (loop [url url, data nil]
    (if-not (blank? url)
      (let [response (http/get url cfg/request-params)
            response-data (u/parse-response response)]
        (cond
          (= (:status response-data) 200)
          (do
            (recur (:next-page-url response-data)
                   (u/acc-response-data data
                     (json/read-str (:body response)))))
          (= (:status response-data) 429)
          (do
            (Thread/sleep (u/calc-timeout (:limit-reset response-data)))
            (recur url data))))
      data)))

(defn fetch-user [identity]
  (fetch (cfg/user-url identity)))

(defn fetch-followers [user]
  (map #(get % "follower")
       (fetch (get user "followers_url"))))

(defn fetch-shots [follower]
  (fetch (get follower "shots_url")))

(defn fetch-likers [shot]
  (map #(get % "user")
       (fetch (get shot "likes_url"))))

(defn fetch-top-likers [shots]
  (let [likers (mapcat fetch-likers shots)
        liker-ids (u/get-ids likers)
        formatted (u/prepare-users-data liker-ids likers)]
    (->> liker-ids
         frequencies
         (u/sort-by-followers formatted)
         reverse
         (take 10)
         (u/prepare-top-likers formatted))))
