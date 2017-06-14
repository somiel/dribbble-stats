(ns dribbble-stats.utils)

(defn calc-timeout [delay-ms limit-reset]
  (+ delay-ms (- (* 1000 (bigdec limit-reset))
                 (System/currentTimeMillis))))

(defn sort-by-followers [users user-ids]
  (sort-by
    (fn [[id amount]] [amount (get-in users [id "followers_count"])])
    user-ids))

(defn prepare-users-data [user-ids users]
  (into {}
    (map vector user-ids users)))

(defn get-ids [users]
  (map
    #(get % "id")
    users))

(defn prepare-top-likers [users user-ids]
  (map
    (fn [[user-id amount]] [amount (get users user-id)])
    user-ids))

(defn acc-response-data [acc new-data]
  (if (vector? new-data)
      (concat acc new-data)
      (merge acc new-data)))

(defn parse-response [response]
  {:status (get response :status)
   :next-page-url (get-in response [:links :next :href])
   :limit-reset (get-in response [:headers :X-RateLimit-Reset])})
