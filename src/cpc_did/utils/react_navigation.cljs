(ns cpc-did.utils.react-navigation
  (:require ["react-navigation" :as router :refer (NavigationActions)]
            ["react-navigation-tabs" :refer (createBottomTabNavigator)]
            ["react-navigation-stack" :refer (createStackNavigator)]
            [reagent.core :as r]
            [cpc-did.styles.components.header :as h-styles]
            [cpc-did.components.header :as header]
            [cpc-did.components.m-icon :as m-icon]))

;;;; The namespace is used to wrap component with navigationOptions
;;;; NavigationOptions is a static property for React Component
;;;; We can add it as same as to add a property for JS Object

;;; Do some transfer.
(defn keyword-size
  "Use for transforming JavaScript Object to CLJS Map
  eg: (def a #js {:foo 1, :bar 2, :baz #js [1 2 3]})

      (js->clj a :keywordize-keys true)
      ;;=> {:foo 1, :bar 2, :baz [1 2 3]}

      (js->clj a)
      ;;=> {\"foo\" 1, \"bar\" 2, \"baz\" [1 2 3]}
  Then you can operate it as CLJS Map"
  [obj]
  (js->clj obj :keywordize-keys true))

(defn navigation->state
  "Take navigation info and transform it into CLJS Map"
  [^js navigation]
  (-> navigation .-state keyword-size))


;;; Warp a component with static "navigationOptions"
;;; Also use "reactify-component" to do "reagent->react" transformation

(defn wrap-stack-screen
  "Wrap component with navigationOptions, which works for StackNavigator screens
  We can see available keys/config list:
    https://reactnavigation.org/docs/en/stack-navigator.html#navigationoptions-for-screens-inside-of-the-navigator
  Don't try to config screen Header in it.
  parameters:
  @param component   a reagent component
  @param & props     use destructure to get config parameters"
  [component {:keys [header-title
                     header-left
                     header-right
                     header] :as options}]
  (let [c (r/reactify-component
            (fn [{:keys [navigation]}]
              [component navigation (navigation->state navigation)]))]
    (aset c "navigationOptions"
          (fn [^js navigation]
            (clj->js (merge
                       {:headerStyle {:borderBottomWidth 0
                                      :elevation         0}}
                       (if (contains? options :header) {:header header} {})
                       (if header-title
                         {:headerTitleContainerStyle {:justifyContent "center"}
                          :headerTitleStyle          h-styles/string-header-title-style
                          :headerTitle               header-title}
                         {})
                       (if header-left {:headerLeft               (r/as-element (case (:type header-left)
                                                                                  "stack" [header/stack-page-left
                                                                                           (.-navigation navigation)
                                                                                           (:title header-left)
                                                                                           (:back-func header-left)]
                                                                                  "tab" [header/tab-page-left (:title header-left)]))
                                        :headerLeftContainerStyle h-styles/header-left-container-style} {:headerLeft nil})
                       (if header-right {:headerRight               (r/as-element [header/header-right (:on-press header-right)])
                                         :headerRightContainerStyle h-styles/header-right-container-style} {})))))
    c))

(defn wrap-bottom-tab-screen
  "Wrap component with navigationOptions, which works for BottomTabNavigator screens
   We can see available keys/config list:
         https://reactnavigation.org/docs/en/bottom-tab-navigator.html#navigationoptions-for-screens-inside-of-the-navigator
   parameters:
   @param component   a StackNavigator
   @param & props     use destructure to get config parameters"
  [component {:keys [tab-bar-label                          ;string
                     tab-bar-icon                           ;map with key `:active-icon` and `:icon`
                     tab-bar-visible
                     icon-style
                     tab-bar-on-press]}]
  (aset component "navigationOptions"
        (fn [^js navigation]
          (clj->js (merge
                     {:tabBarLabel   (fn [^js obj]
                                       (m-icon/bottom-tab-label
                                         (.-focused obj)
                                         tab-bar-label))
                      :tabBarVisible (if (identical? tab-bar-visible nil)
                                       true
                                       tab-bar-visible)
                      :tabBarIcon    (fn [^js obj]
                                       (m-icon/bottom-tab-item
                                         (.-focused obj)
                                         (:active-icon tab-bar-icon)
                                         (:icon tab-bar-icon)
                                         icon-style))}
                     (if tab-bar-on-press {:tabBarOnPress tab-bar-on-press} {})))))
  component)


;;; Wrap some "create" navigator function of "react-navigation", so
;;; we needn't to import "react-navigation" in other namespace

(defn make-stack-navigator
  "Take a route configuration and navigator configs to
  set up an actual stack navigator"
  [route-configs navigator-configs]
  (createStackNavigator
    (clj->js
      (into {}
            (for [config route-configs]
              (assoc {}
                (:route-name config)
                {:screen (if (:is-navigator config)
                           (:page config)
                           (wrap-stack-screen
                             (:page config)
                             (:nav-options config)))}))))
    (clj->js navigator-configs)))

(defn make-bottom-tab-navigator
  "Take a route configs and navigator configs to
  set up an actual bottom-tab navigator"
  [route-configs navigator-configs]
  (createBottomTabNavigator
    (clj->js
      (into {}
            (for [config route-configs]
              (assoc {}
                (:route-name config)
                {:screen (wrap-bottom-tab-screen
                           (:page config)
                           (:nav-options config))}))))
    (clj->js navigator-configs)))

(defn make-switch-navigator
  [route-configs navigator-configs]
  (router/createSwitchNavigator
    (clj->js
      (into {}
            (for [config route-configs]
              (assoc {}
                (:route-name config)
                {:screen (:page config)}))))
    (clj->js navigator-configs)))

(defn make-app-container
  "Take a navigator to set up an actual app container"
  [navigator]
  (router/createAppContainer navigator))

(defn nav-reset
  "Pass variadic params to function, will as a list format"
  [^js navigation route-name & params]
  (.reset navigation #js [(.navigate ^js NavigationActions #js {:routeName route-name
                                                                :params    (first params)})] 0))
;;; Some macro to simplify operation
;;; Will have soon