(ns cpc-did.components.static-assets)

;;; logo
(defonce logo-cpc (js/require "../resources/assets/imgs/logo.png"))
(defonce logo-background (js/require "../resources/assets/imgs/logo_bg.png"))
(defonce logo-GAC-horizontal (js/require "../resources/assets/imgs/logo_GAC_horizontal.png"))

;;; form relevant
(defonce input-passed (js/require "../resources/assets/imgs/ic_input_passed.png"))
(defonce input-fail (js/require "../resources/assets/imgs/ic_error.png"))
(defonce qr-scan (js/require "../resources/assets/imgs/icon_scan.png"))

;;; avatar
(defonce avatar (js/require "../resources/assets/imgs/avatar.png"))
(defonce avatar-res (js/require "../resources/assets/imgs/avatar_res.png"))

;;; list relevant
;; ident core
(defonce attestation-vehicle (js/require "../resources/assets/imgs/ic_vehicleattest.png"))
(defonce logo-GAC-vertical (js/require "../resources/assets/imgs/logo_GAC_vertical.png"))
(defonce arrow-enter (js/require "../resources/assets/imgs/arrow_enter.png"))
;; record relevant
(defonce list-car (js/require "../resources/assets/imgs/icon_list_car.png"))
;; select credential
(defonce circle-checked (js/require "../resources/assets/imgs/ic_checked.png"))
(defonce circle-unchecked (js/require "../resources/assets/imgs/ic_unchecked.png"))

;;; list detail
;; record list detail
(defonce user-name (js/require "../resources/assets/imgs/ic_name.png"))
(defonce id-card (js/require "../resources/assets/imgs/ic_IDCard.png"))
(defonce vin (js/require "../resources/assets/imgs/ic_VIN.png"))
(defonce credential-record-num (js/require "../resources/assets/imgs/ic_credentials_record.png"))
(defonce auth-record-num (js/require "../resources/assets/imgs/ic_authorizations_record.png"))
(defonce pending (js/require "../resources/assets/imgs/ic_pending.png"))
(defonce success (js/require "../resources/assets/imgs/ic_success.png"))
(defonce passed (js/require "../resources/assets/imgs/ic_passed.png"))

;;; bottom tab relevant
(defonce identification-unselected (js/require "../resources/assets/imgs/icon_attest_unchecked.png"))
(defonce identification-selected (js/require "../resources/assets/imgs/icon_attest_checked.png"))
(defonce qr-code (js/require "../resources/assets/imgs/qr_code.png"))
(defonce me-unselected (js/require "../resources/assets/imgs/icon_mine_unchecked.png"))
(defonce me-selected (js/require "../resources/assets/imgs/icon_mine_checked.png"))

;;; alert modal relevant
(defonce disable-screen-snapshot (js/require "../resources/assets/imgs/ic_ScreenCaptureDisabled.png"))
(defonce close (js/require "../resources/assets/imgs/ic_close.png"))

;;; background relevant
(defonce credential-background (js/require "../resources/assets/imgs/bg_credential.png"))
(defonce authorization-background (js/require "../resources/assets/imgs/authorization_bg.png"))

;;; user core
(defonce authorization_record (js/require "../resources/assets/imgs/ic_authorizations.png"))
(defonce credential-record (js/require "../resources/assets/imgs/ic_credentials.png"))
(defonce keystore-backup (js/require "../resources/assets/imgs/ic_backup.png"))

;;; header relevant
(defonce more (js/require "../resources/assets/imgs/ic_more.png"))
(defonce more-black (js/require "../resources/assets/imgs/ic_more_black.png"))
(defonce arrow-back (js/require "../resources/assets/imgs/arrow_back.png"))
(defonce arrow-back-black (js/require "../resources/assets/imgs/arrow_back_black.png"))

;;; button relevant
(defonce question (js/require "../resources/assets/imgs/ic_question.png"))

;;; animations
(defonce ani-loading (js/require "../resources/assets/animations/loading.json"))
(defonce toast-loading (js/require "../resources/assets/animations/toast_loading.json"))

;;; shadow
(defonce shadow-authorize (js/require "../resources/assets/imgs/shadow_authorize.png"))

;;; auth and ident result
(defonce res-success (js/require "../resources/assets/imgs/ic_res_success.png"))
(defonce res-failure (js/require "../resources/assets/imgs/ic_res_failure.png"))
(defonce res-warning (js/require "../resources/assets/imgs/ic_warning.png"))

;;; toast
(defonce toast-error (js/require "../resources/assets/imgs/ic_toast_error.png"))

;;; others
(defonce safe-icon (js/require "../resources/assets/imgs/ic_safe.png"))
(defonce logo-GAC-intro (js/require "../resources/assets/imgs/logo_GAC_intro.png"))
