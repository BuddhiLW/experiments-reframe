(ns playground.auth0
  (:require [clj-http.client :as http]
            [muuntaja.core :as m]))

(defn get-test-token
  []
  (->> {:content-type :json
        :body
        (m/encode "application/json"
                  {:client_id "bCusSIxOJa433PtnfDCoyDplkmlxH3wV"
                   :client_secret "EHsoQt2dzL9Gj8jcZ0XWQT0aDbaAXxYD7dvp_wZtOmRDBy_7KCC8VrDyR0QgjTbz"
                   :audience "https://dev-fxcu8cjbww7ragnp.us.auth0.com/api/v2/"
                   :grant_type "password"
                   :username "testing@playground.app"
                   :password "testing@playground.app1!"
                   :scope "openid profile email"})}
       (http/post "https://dev-fxcu8cjbww7ragnp.us.auth0.com/oauth/token")
       (m/decode-response-body)
       :access_token))

(comment
  (->> {:content-type :json
        :body
        (m/encode "application/json"
                  {:client_id "KoH7tEPpWmmTvXWleeDNwY0yPlxDuRxn"
                   :client_secret "_5sWuJVmicZ5z-GsRdnm23_LuiW8mcyxmSQvoMsUqXuoMM1DL1Oi36F7XqP3JklE"
                   :audience "https://dev-fxcu8cjbww7ragnp.us.auth0.com/api/v2/"
                   :grant_type "client_credentials"})}
       (http/post "https://dev-fxcu8cjbww7ragnp.us.auth0.com/oauth/token")
       (m/decode-response-body)))
