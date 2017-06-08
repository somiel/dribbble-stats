(ns dribbble-stats.config)

(def api-key "e84f1d3f7bd4c6476b4f63d2ff9ad6c2ba95dfe73c5373db788c32366c7bffc3")

(def root-url "https://api.dribbble.com/v1/")

(defn user-url [identity]
  (str root-url "users/" identity))

(def request-params {:throw-exceptions false
                     :as :json
                     :query-params {:access_token api-key}})
