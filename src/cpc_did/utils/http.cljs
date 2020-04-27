(ns cpc-did.utils.http
  (:require ["js-base64" :refer (Base64)]
            ["ethers" :refer (ethers)]
            ["@react-native-community/netinfo" :as NetInfo]
            [venia.core :as v]
            [cljs-http.client :as http]
            [cpc-did.utils.error :as err]
            [cpc-did.utils.cljs-js-transit :as t]
            [cpc-did.db :refer [vc->storage]]
            [cpc-did.utils.event-listener :as listener :refer [emit]]
            [re-frame.core :refer [dispatch subscribe]]
            [cljs.core.async :refer [>! <! chan close!]])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [cpc-did.utils.macros :refer [promise->]]))
(def ^:dynamic *TIMEOUT* 10000)

(def GET_CLAIM_INDEX "GET_CLAIM_INDEX")
(def LOADING_GET_CLAIM_INDEX "LOADING_GET_CLAIM_INDEX")
(def GET_CLAIM "GET_CLAIM")
(def REGISTER_DID "REGISTER_DID")
(def GET_CREDENTIAL "GET_CREDENTIAL")
(def GET_DATA_WITHOUT_TOKEN "GET_DATA_WITHOUT_TOKEN")
(def GET_VERIFICATION "GET_VERIFICATION")
(def GET_VERIFICATION_WITHOUT_REGISTER "GET_VERIFICATION_WITHOUT_REGISTER")
(def CLAIM_INDEX_REFRESH "CLAIM_INDEX_REFRESH")

(def url "http://did-test-env-lb-1193053172.ap-southeast-1.elb.amazonaws.com/graphql")
(def GuangQi-did "did:cpchain:403e7df19263d9d7646b11ebfc125f3dd6c08619")

;;;-- helper function ---------------------------------------------------------
(defn query
  [data]
  (str "query " data))

(defn mutation
  [data]
  (str "mutation " data))

(defn base64-decode
  [data]
  (-> data
      (#(.decode ^js Base64 %))
      (#(.parse js/JSON %))
      t/->cljs))

(defn base64-encode
  [data]
  (-> data
      t/->js
      (#(.stringify js/JSON %))
      (#(.encode ^js Base64 %))))

(defn get-public-key
  [wallet]
  (-> ^js wallet
      .-signingKey
      .-publicKey
      (subs 4)
      (#(str "0x" %))))

(defn sign-message
  "Give a signature for a string"
  [^js wallet data]
  (.signMessage wallet (-> ^js ethers
                           .-utils
                           (.toUtf8Bytes data))))

;;; -- check network state ---------------------------------------------------
;;; error process in function own
(defn get-network-state
  []
  (promise-> #(println %)
             (.. NetInfo
                 -isConnected
                 fetch)
             #(if %
                (js/Promise.resolve true)
                (do
                  (err/error {:code 605 :err "bad network"})
                  (js/Promise.resolve false)))))

;;; -- http request ---------------------------------------------------------

(defn get-claim-index
  "A claim list with issuer
  Some useful infos to query claim details."
  []
  (let [claim-list (chan 1 (map #(-> % :body :data :QueryClaimIndex :claimIndex)))
        issuer (chan 1 (map #(-> % :body :data :QueryClaimIndex :did)))
        success (chan 1)]
    (go (let [response (<! (http/post url
                                      {:json-params
                                               {:query (query
                                                         (v/graphql-query
                                                           {:venia/queries
                                                            [[:QueryClaimIndex {:input {:did GuangQi-did}}
                                                              [:did [:claimIndex [:type :uri]]]]]}))}
                                       :timeout *TIMEOUT*}))]
          (cond
            (= (:status response) -1) (do
                                        (err/error {:code 408 :err "require timeout"})
                                        (>! success true))
            :else (do
                    (>! claim-list response)
                    (>! issuer response)
                    (>! success true)))))
    (js/Promise.resolve {:list claim-list :issuer issuer :success success})))

(defn get-claim
  "Get a claim detail"
  [query-claim]
  (let [claim (chan 1 (map #(-> % :body :data :GetClaim :claim)))
        success (chan 1)]
    (go (let [response (<! (http/post url
                                      {:json-params
                                       {:query (query
                                                 (v/graphql-query
                                                   {:venia/queries
                                                    [[:GetClaim {:input {:issuerDid GuangQi-did
                                                                         :type      (:type query-claim)
                                                                         :uri       (:uri query-claim)}}
                                                      [:claim :downloadStatus]]]}))}
                                       :timeout *TIMEOUT*}))]
          (cond
            (= (:status response) -1) (do
                                        (err/error {:code 408 :err "require timeout"})
                                        (>! success false))
            :else (do
                    (>! claim response)
                    (>! success true)))))
    (js/Promise.resolve {:claim claim :success success})))

(defn get-nonce
  "Get a nonce"
  [did public-key]
  (let [nonce (chan 1 (map #(-> % :body :data :AuthRequest :nonce)))
        success (chan 1)]
    (go (let [response (<! (http/post url
                                      {:json-params
                                                {:query (mutation
                                                          (v/graphql-query
                                                            {:venia/queries
                                                             [[:AuthRequest {:input {:document {:did       did
                                                                                                :publicKey [{:value      public-key
                                                                                                             :controller did
                                                                                                             :type       'ECC}]}}}
                                                               [:nonce]]]}))}
                                       :timeout *TIMEOUT*}))]
          (cond
            (= (:status response) -1) (do
                                        (err/error {:code 408 :err "require timeout"})
                                        (>! success false))
            :else (do
                    (>! nonce response)
                    (>! success true)))))
    (js/Promise.resolve {:nonce nonce :success success})))

(defn get-token
  [^js wallet did data]
  (let [token (chan 1 (map #(-> % :body :data :AuthConfirm :token)))
        success (chan 1)]
    (go (if (<! (:success data))
          (let [nonce (<! (:nonce data))]
            (promise-> #(println %)
                       (.signMessage wallet (str did nonce))
                       #(go (let [response (<! (http/post url
                                                          {:json-params
                                                                    {:query (mutation
                                                                              (v/graphql-query
                                                                                {:venia/queries
                                                                                 [[:AuthConfirm {:input {:did       did
                                                                                                         :nonce     nonce
                                                                                                         :signature %}}
                                                                                   [:token]]]}))}
                                                           :timeout *TIMEOUT*}))]
                              (cond
                                (= (:status response) -1) (do
                                                            (err/error {:code 408 :err "require timeout"})
                                                            (>! success false))
                                (nil? (-> response :body :data :AuthConfirm)) (err/error {:code 403 :err "can't get token"})
                                :else (do
                                        (>! token response)
                                        (dispatch [:add-token (<! token)])
                                        (>! token response)
                                        (>! success true)))))))
          (>! success false)))
    (js/Promise.resolve {:token token :success success})))

(defn register-did
  "Register did, give a public-key to sever, which use to verify."
  [public-key did data]
  (let [result (chan 1 (map #(-> % :body :data :RegisterDID)))
        success (chan 1)]
    (go (if (<! (:success data))
          (let [response (<! (http/post
                               url
                               {:json-params
                                         {:query (mutation
                                                   (v/graphql-query
                                                     {:venia/queries
                                                      [[:RegisterDID {:input {:document {:did       did
                                                                                         :publicKey [{:value      public-key
                                                                                                      :controller did
                                                                                                      :type       'ECC}]}
                                                                              :token    (<! (:token data))}}
                                                        [:status :uri]]]}))}
                                :timeout *TIMEOUT*}))]
            (cond
              (= (:status response) -1) (do
                                          (err/error {:code 408 :err "require timeout"})
                                          (>! success false))
              (nil? (-> response :body :data :RegisterDID)) (do
                                                              (err/error {:code 401 :err "register did failed"})
                                                              (>! success false))
              :else (do
                      (>! result response)
                      (>! success true))))
          (>! success false)))
    (js/Promise.resolve {:result result :success success})))

(defn get-credential
  "Get credential"
  [claim token]
  (let [credential (chan 1 (map #(-> % :body :data :RequestCred)))
        success (chan 1)]
    (go (let [response (<! (http/post url
                                      {:json-params
                                                {:query (query
                                                          (v/graphql-query
                                                            {:venia/queries
                                                             [[:RequestCred {:input {:claim claim
                                                                                     :did   GuangQi-did
                                                                                     :token token}}
                                                               [:cred :credId]]]}))}
                                       :timeout *TIMEOUT*}))]
          (cond
            (= (:status response) -1) (do
                                        (err/error {:code 408 :err "require timeout"})
                                        (>! success false))
            (nil? (-> response :body :data :RequestCred)) (do
                                                            (err/error {:code 703 :err "invalid token"})
                                                            (>! success false))
            :else (do
                    (>! credential response)
                    (>! success true)))))
    (js/Promise.resolve {:credential credential
                         :success    success})))

(defn get-verId
  [verifier]
  (let [verId (chan 1 (map #(-> % :body :data :RequestVer :verId)))
        success (chan 1)]
    (go (let [response (<! (http/post url
                                      {:json-params
                                                {:query (query
                                                          (v/graphql-query
                                                            {:venia/queries
                                                             [[:RequestVer {:input {:did         @(subscribe [:cpc-id])
                                                                                    :verifierDid verifier}}
                                                               [:verId]]]}))}
                                       :timeout *TIMEOUT*}))]
          (cond
            (= (:status response) -1) (do
                                        (err/error {:code 408 :err "require timeout"})
                                        (>! success false))
            :else (do
                    (>! verId response)
                    (>! success true)))))
    (js/Promise.resolve {:verId verId :success success})))

(defn get-verification
  "Get verification, which means verifying a credential"
  [presentation token verId]
  (let [verify-vp (chan 1 (map #(-> % :body :data :VerifyVP)))
        success (chan 1)]
    (go (let [response (<! (http/post url
                                      {:json-params
                                                {:query (mutation
                                                          (v/graphql-query
                                                            {:venia/queries
                                                             [[:VerifyVP {:input {:did          GuangQi-did
                                                                                  :presentation presentation
                                                                                  :token        token
                                                                                  :verId        verId}}
                                                               [:vpVerId]]]}))}
                                       :timeout *TIMEOUT*}))]
          (cond
            (= (:status response) -1) (do
                                        (err/error {:code 408 :err "require timeout"})
                                        (>! success false))
            (nil? (-> response :body :data :VerifyVP)) (do
                                                         (err/error {:code 703 :error "invalid token"})
                                                         (>! success false))
            :else (do
                    (>! verify-vp response)
                    (>! success true)))))
    (js/Promise.resolve {:verify-vp verify-vp
                         :success   success})))

;;; -- wrap request --------------------------------------------------------

(defn get-claim-index-wrapper
  "Write claim list to db and storage"
  [network]
  (if network
    (promise-> #(println %)
               (get-claim-index)
               (fn [data]
                 (go (if (<! (:success data))
                       (dispatch [:add-claim-list (<! (:list data)) (<! (:issuer data))])))))))

(defn claim-index-refresh
  "Write claim list to db and storage"
  [network]
  (if network
    (promise-> #(println %)
               (get-claim-index)
               (fn [data]
                 (go (let [success (<! (:success data))]
                       (if success
                         (dispatch [:add-claim-list (<! (:list data)) (<! (:issuer data))]))
                       (emit listener/HTTP_SUCCESS nil)))))
    (emit listener/HTTP_SUCCESS nil)))

(defn get-claim-wrapper
  "Write claim detail to db and storage"
  [network query-claim]
  (if network
    (promise-> #(println %)
               (get-claim query-claim)
               (fn [data]
                 (go (if (<! (:success data))
                       (dispatch [:add-claim-detail (base64-decode (<! (:claim data)))])))))))

(defn register-did-wrapper
  "Always navigate to next page, whatever the register result"
  [network {:keys [wallet did]}]
  (if network
    (let [public-key (get-public-key wallet)]
      (promise-> #(println %)
                 (get-nonce did public-key)
                 #(get-token wallet did %)
                 #(register-did public-key did %)
                 (fn [data]
                   (go (if (<! (:success data))
                         (dispatch [:is-register-did true])))
                   (emit listener/HTTP_SUCCESS nil))))
    (emit listener/HTTP_SUCCESS nil)))

(defn get-credential-wrapper
  "Get a credential
  It's useful for get-data-without-token"
  [network {:keys [claim token]}]
  (if network
    (promise-> #(println %)
               (get-credential claim token)
               (fn [data]
                 (go (if (<! (:success data))
                       (let [cred (<! (:credential data))
                             m-cred (atom {})]
                         (-> (:cred cred)
                             base64-decode
                             ((fn [data]
                                (swap! m-cred
                                       assoc
                                       :create-time (:created data)
                                       :expire-time (:expire data)
                                       :state "认证通过")
                                (-> (:claim data)
                                    base64-decode
                                    ((fn [data]
                                       (swap! m-cred
                                              assoc
                                              :username (:username data)
                                              :idCard (:idCard data)
                                              :VIN (:VIN data)
                                              :type (-> data :type (.slice 0 -2)))))))))
                         (dispatch [:add-cert @m-cred])
                         (vc->storage cred)
                         (emit listener/HTTP_SUCCESS @m-cred))))))
    (emit listener/HTTP_ERROR {:code 605 :message "网络异常"})))

(defn get-data-without-token
  "Get a data without token, which means that need get token first.
  Used for:
  1. get credential
  2. get verification"
  [network {:keys [event wallet] :as data}]
  (if network
    (let [did @(subscribe [:cpc-id])
          public-key (get-public-key wallet)]
      (promise-> #(println %)
                 (get-nonce did public-key)
                 #(get-token wallet did %)
                 (fn [result]
                   (go (if (<! (:success result))
                         (event true (merge data {:token (<! (:token result))})))))))
    (emit listener/HTTP_ERROR {:code 605 :message "网络异常"})))


(defn get-verification-wrapper
  "Get verification and write to db, storage"
  [network {:keys [verifier wallet vc holder
                   location create-time expire-time
                   token type supplier]}]
  (if network
    (promise-> #(println %)
               (get-verId verifier)
               (fn [data]
                 (go (if (<! (:success data))
                       (let [verId (<! (:verId data))]
                         (promise-> #(println %)
                                    (sign-message
                                      wallet
                                      (str vc holder verifier
                                           location create-time
                                           expire-time))
                                    #(base64-encode {:vc        vc
                                                     :holder    holder
                                                     :verifier  verifier
                                                     :location  location
                                                     :created   create-time
                                                     :expire    expire-time
                                                     :signature %})
                                    #(get-verification % token verId)
                                    (fn [data]
                                      (go (if (<! (:success data))
                                            (let [v-detail {:type     type
                                                            :location location
                                                            :verifier verifier
                                                            :supplier supplier
                                                            :state    "验证成功"
                                                            :created  (.toISOString ^js (js/Date.))}]
                                              (dispatch [:add-auth v-detail])
                                              (emit listener/HTTP_SUCCESS v-detail)))))))))))
    (emit listener/HTTP_ERROR {:code 605 :message "网络异常"})))

(defn get-verification-without-register
  "Without register, can't get veification.
  So, we need register the did first."
  [network {:keys [wallet holder] :as data}]
  (if network
    (let [public-key (get-public-key wallet)]
      (promise-> #(println %)
                 (get-nonce holder public-key)
                 #(get-token wallet holder %)
                 #(register-did public-key holder %)
                 (fn [result]
                   (go (if (<! (:success result))
                         (do
                           (dispatch [:is-register-did true])
                           (get-verification-wrapper true (merge data
                                                                 {:token @(subscribe [:token])}))))))))
    (emit listener/HTTP_ERROR {:code 605 :message "网络异常"})))

(defn request-wrapper
  "add network check to request"
  [event data]
  (promise-> #(println %)
             (get-network-state)
             (fn [network]
               (condp = event
                 LOADING_GET_CLAIM_INDEX (binding [*TIMEOUT* 2000]
                                           (get-claim-index-wrapper network))
                 GET_CLAIM_INDEX (get-claim-index-wrapper network)
                 GET_CLAIM (get-claim-wrapper network data)
                 REGISTER_DID (register-did-wrapper network data)
                 GET_CREDENTIAL (get-credential-wrapper network data)
                 GET_DATA_WITHOUT_TOKEN (get-data-without-token network data)
                 GET_VERIFICATION (get-verification-wrapper network data)
                 GET_VERIFICATION_WITHOUT_REGISTER (get-verification-without-register network data)
                 CLAIM_INDEX_REFRESH (claim-index-refresh network)
                 nil))))
