(ns cpc-did.router
  (:require
    [cpc-did.styles.navigator :as nav-styles]
    [cpc-did.components.static-assets :as assets]
    [cpc-did.utils.react-navigation :as r-nav]
    [cpc-did.styles.utils.layout :as layout]
    ;; identification pages
    [cpc-did.pages.identification.core :as ident-main]
    [cpc-did.pages.identification.institution_intro :as inst-intro]
    [cpc-did.pages.identification.input-identification-info :as ident-info]
    [cpc-did.pages.identification.certification-detail :as cert-detail]
    [cpc-did.pages.identification.vin-scanner :as vin-scanner]

    ;; qr-code pages
    [cpc-did.pages.qr-code.core :as qr-main]
    [cpc-did.pages.qr-code.qr-code :as qr-code]
    [cpc-did.pages.qr-code.authorize :as authorize]
    [cpc-did.pages.qr-code.authorization-detail :as auth-detail]
    [cpc-did.pages.qr-code.auth-failure :as failure]
    [cpc-did.pages.qr-code.auth-success :as success]

    ;; user pages
    [cpc-did.pages.user.core :as user-main]
    [cpc-did.pages.user.certification-records :as cert-records]
    [cpc-did.pages.user.authorization-records :as auth-records]
    [cpc-did.pages.user.auth-record-detail :as auth-record-detail]

    ;; help pages
    [cpc-did.pages.help.vin-help :as vin-help]

    ;; start pages
    [cpc-did.pages.start-page.backup-id :as backup]
    [cpc-did.pages.start-page.core :as main]
    [cpc-did.pages.start-page.create-id :as create-id]
    [cpc-did.pages.start-page.create-result :as create-result]
    [cpc-did.pages.start-page.import-id :as import-id]
    [cpc-did.pages.start-page.import-username :as import-username]
    ;;app loading
    [cpc-did.pages.app-loading :as auth-loading]))
;;;; App router


;;; --re-frame start-------------------------------------------
;(rf/dispatch-sync [:initialize-db])

;;; -- Some Options -------------------------------------------
(def ident-nav-options {:header-title nil
                        :header-left  {:type "tab" :title "认证申请"}
                        :header-right {:on-press nil}})

(def ident-nav-nav-options {:tab-bar-label "认证申请"
                            :tab-bar-icon  {:active-icon assets/identification-selected
                                            :icon        assets/identification-unselected}})

(def qr-nav-nav-options {:tab-bar-label    " "
                         :tab-bar-icon     {:active-icon assets/qr-code
                                            :icon        assets/qr-code}
                         :tab-bar-visible  false
                         :icon-style       layout/qr-tab-style
                         :tab-bar-on-press ident-main/show-toast})

(def user-nav-nav-options {:tab-bar-label "我的"
                           :tab-bar-icon  {:active-icon assets/me-selected
                                           :icon        assets/me-unselected}
                           :labelStyle    {:font-size 20}})
(def without-header {:header nil})

;;; identification relevant pages
(def inst-intro-nav-option {:header-left {:type  "stack"
                                          :title "认证详情"
                                          :back-func {:method "pop"}}})
(def ident-info-nav-option {:header-left {:type  "stack"
                                          :title "认证申请"}})

(def cert-detail-nav-option {:header-left {:type       "stack"
                                           :title      "认证详情"
                                           :back-func {:method "reset"
                                                       :route-name "tab-nav"}}})

;;; authorization pages
(def authorize-nav-option {:header-left {:type  "stack"
                                         :title "选择证书"}})

;;; user relevant pages
(def cert-records-nav-option {:header-left {:type  "stack"
                                            :title "我的认证"}})
(def auth-records-nav-option {:header-left {:type  "stack"
                                            :title "验证记录"}})
(def user-backup-nav-option {:header-left {:type  "stack"
                                           :title "CPC ID备份"}})
(def cert-record-detail-nav-option {:header-left {:type  "stack"
                                                  :title "认证详情"}})
;;; start pages
(def start-backup-nav-option {:header-left {:type       "stack"
                                            :title      "CPC ID备份"
                                            :back-func {:method "reset"
                                                        :route-name "tab-nav"}}})
(def import-id-nav-option {:header-left {:type       "stack"
                                         :title      "导入CPC ID"}})
(def create-id-nav-option {:header-left {:type       "stack"
                                         :title      "创建CPC ID"}})
(def create-loading-nav-option {:header nil})
(def create-result-nav-option {:header-title "CPC ID"})
(def import-username-nav-option {:header-title "CPC ID"})

;;; help pages
(def vin-help-nav-option {:header-left {:type       "stack"
                                        :title      "什么是VIN"}})

;;; -- Navigator ----------------------------------------------
;;; Different navigator can nest, compose them to implement more functions.

(def ident-stack
  (r-nav/make-stack-navigator
    [{:route-name  :identification
      :page        ident-main/page
      :nav-options ident-nav-options}]
    {:initialRouteName "identification"}))

(def qr-code-stack
  (r-nav/make-stack-navigator
    [{:route-name  :qr-main
      :page        qr-main/page
      :nav-options without-header}]
    {:initialRouteName "qr-main"}))

(def user-stack
  (r-nav/make-stack-navigator
    [{:route-name  :user
      :page        user-main/page
      :nav-options without-header}]
    {:initialRouteName "user"}))

(def tab-nav
  (r-nav/make-bottom-tab-navigator
    [{:route-name  :ident-nav
      :page        ident-stack
      :nav-options ident-nav-nav-options}
     {:route-name  :qr-nav
      :page        qr-code-stack
      :nav-options qr-nav-nav-options}
     {:route-name  :user-nav
      :page        user-stack
      :nav-options user-nav-nav-options}]
    {:tabBarOptions     {:style nav-styles/bottom-tab-style}
     :navigationOptions {:header nil}
     :resetOnBlur true
     :initialRouteName "ident-nav"}))

(def app-stack
  (r-nav/make-stack-navigator
    [{:route-name   :tab-nav
      :page         tab-nav
      :is-navigator true}

     ;; identification relevant
     {:route-name  :inst-intro
      :page        inst-intro/page
      :nav-options inst-intro-nav-option}
     {:route-name  :ident-info
      :page        ident-info/page
      :nav-options ident-info-nav-option}
     {:route-name  :cert-detail
      :page        cert-detail/page
      :nav-options cert-detail-nav-option}
     {:route-name  :vin-scanner
      :page        vin-scanner/page
      :nav-options without-header}

     ;; user relevant
     {:route-name  :cert-records
      :page        cert-records/page
      :nav-options cert-records-nav-option}
     {:route-name  :auth-records
      :page        auth-records/page
      :nav-options auth-records-nav-option}
     {:route-name  :user-backup
      :page        backup/page
      :nav-options user-backup-nav-option}
     {:route-name  :auth-record-detail
      :page        auth-record-detail/page
      :nav-options without-header}
     {:route-name  :cert-record-detail
      :page        cert-detail/page
      :nav-options cert-record-detail-nav-option}

     ;; authorize relevant
     {:route-name :qr-code
      :page qr-code/page
      :nav-options without-header}
     {:route-name  :authorize
      :page        authorize/page
      :nav-options authorize-nav-option}
     {:route-name  :auth-detail
      :page        auth-detail/page
      :nav-options without-header}
     {:route-name  :auth-failure
      :page        failure/page
      :nav-options without-header}
     {:route-name  :auth-success
      :page        success/page
      :nav-options without-header}

     ;;help page
     {:route-name        :vin-help
      :page              vin-help/page
      :nav-options vin-help-nav-option}]
    {:initialRouteName "tab-nav"}))

(def start-stack
  (r-nav/make-stack-navigator
    [{:route-name  :main
      :page        main/page
      :nav-options without-header}
     {:route-name  :import-id
      :page        import-id/page
      :nav-options import-id-nav-option}
     {:route-name  :import-username
      :page        import-username/page
      :nav-options import-username-nav-option}
     {:route-name  :create-id
      :page        create-id/page
      :nav-options create-id-nav-option}
     {:route-name  :create-result
      :page        create-result/page
      :nav-options create-result-nav-option}
     {:route-name  :start-backup
      :page        backup/page
      :nav-options start-backup-nav-option}]
    {:initialRouteName "main"}))

(def auth-stack
  (r-nav/make-stack-navigator
    [{:route-name  :auth-loading
      :page        auth-loading/page
      :nav-options without-header}]
    {:initialRouteName "auth-loading"}))

(def default-nav
  (r-nav/make-switch-navigator
    [{:route-name :auth-stack
      :page       auth-stack}
     {:route-name :app-stack
      :page       app-stack}
     {:route-name   :start-stack
      :page         start-stack
      :is-navigator true}]

    {:initialRouteName "auth-stack"}))
(def app-container (r-nav/make-app-container default-nav))

