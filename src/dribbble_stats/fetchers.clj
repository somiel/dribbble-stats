(ns dribbble-stats.fetchers
  (:require [clj-http.client :as http]
            [dribbble-stats.config :as cfg]
            [dribbble-stats.utils :as u]
            [clojure.string :as str :refer [blank?]]))

(def cached-data (atom []))

(defn- fetch [url]
  (loop [url url, data nil]
    (if-not (blank? url)
      (let [response (http/get url cfg/request-params)
            response-data (u/parse-response response)
            response-json (:body-json response-data)]
        (cond
          (= (:status response-data) 200)
          (do
            (u/make-caching cached-data url response-json)
            (recur (:next-page-url response-data)
                   (u/acc-response-data data response-json)))
          (= (:status response-data) 429)
          (do
            (Thread/sleep (u/calc-timeout cfg/api-timeout (:limit-reset response-data)))
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
