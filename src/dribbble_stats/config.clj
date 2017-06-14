(ns dribbble-stats.config)

(def api-key "a800f46b3df8f5f2e5a9ffd2e2d8282f7e48468d715b75af8cbc2d15ae5cbebb")
(def api-timeout 1000)
(def root-url "https://api.dribbble.com/v1/")

(defn user-url [identity]
  (str root-url "users/" identity))

(def request-params {:throw-exceptions false
                     :as :json
                     :query-params {:access_token api-key :per_page 100}})
