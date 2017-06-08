# dribbble-stats

A Clojure tool designed to:

- For a given Dribbble user find all followers
- For each follower find all shots
- For each shot find all "likers"
- Calculate Top10 "likers"

## Usage
```clojure
(require '[dribbble-stats.core :refer [top-likers]])

(top-likers "username or user id")

```

## License

Copyright © 2017 somiel

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
