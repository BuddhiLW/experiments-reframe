(ns playground.middleware
  (:require [ring.middleware.jwt :as jwt]))

(def wrap-auth0
  {:name ::auth0
   :description "Middleware for auth0 authentication and authorization"
   :wrap (fn [handler]
           (jwt/wrap-jwt
            handler
            {:issuers
             {"https://dev-fxcu8cjbww7ragnp.us.auth0.com/" {:alg :RS256
                                                            :jwk-endpoint "https://dev-fxcu8cjbww7ragnp.us.auth0.com/.well-known/jwks.json"}}
             :reject-missing-token? false}))})
