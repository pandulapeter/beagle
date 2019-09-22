# DebugMenu (Android)
*A smart side drawer for debugging your Android apps.*

**This library is in very early stages of development. New features will be added and the API might change.**

### Usage
Add the following to your top level Gradle file:

```groovy
allprojects {
    repositories {
        …
        maven { url "https://jitpack.io" }
    }
}
```

...and this to the module-level build script (check the widget below the code snippet for the latest version):

```groovy
dependencies {
    …
    def debugMenuVersion = "0.0.6"
    debugImplementation "com.github.pandulapeter.debug-menu:debug-menu:$debugMenuVersion"
    releaseImplementation "com.github.pandulapeter.debug-menu:debug-menu-noop:$debugMenuVersion"
}
```

The latest version is:


[![](https://jitpack.io/v/pandulapeter/debug-menu.svg)](https://jitpack.io/#pandulapeter/debug-menu)

The library has to be initialized in the Application class by calling:

```kotlin
DebugMenu.attachToUi(this)
```

After this a list of modules needs to be provided, but this can be changed at any time and the UI will be automatically updated. See [this example](https://github.com/pandulapeter/debug-menu/blob/master/example/src/main/java/com/pandulapeter/debugMenuExample/DebugMenuExampleApplication.kt) for details.

### Modules
The library currently supports the following modules (unique modules can only be added once while generic modules don't have this limitation as long as they are initialized with a unique ID):
* [Header](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/HeaderModule.kt) - This unique module will always stay at the top of the drawer and display general information about the app / build provided by you.
* [SettingsLink](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/SettingsLinkModule.kt) - This unique module displays a button that opens the Android Settings page for your app.
* [KeylineOverlay](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/KeylineOverlayModule.kt) - This unique module displays a switch that, when enabled, draws a grid over your app with configurable dimensions that you can use to check the alignments of your Views.
* [NetworkLogging](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/NetworkLoggingModule.kt) - This unique module will display an expandable list of your OkHttp network activity. Each item can be tapped for more information. To use this functionality, the custom NetworkLoggingInterceptor needs to be added to the OkHTTP Client's builder, as implemented [here](https://github.com/pandulapeter/debug-menu/blob/master/example/src/main/java/com/pandulapeter/debugMenuExample/networking/NetworkingManager.kt).
* [Logging](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/LoggingModule.kt) - This unique module will display an expandable list of your custom logs. Each item can be tapped for more information if you specified a payload. To log an event, simply call DebugMenu.log().
* [ItemList](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/modules/ItemListModule.kt) - This generic module will display an expandable list of items configured by you. When an item is selected, a custom callback gets invoked. Possible use cases could be providing a list of test accounts to make the login process faster or allowing the user to switch between backend environments.

### Customization
* The UI of the drawer can be personalized by specifying a [UiConfiguration](https://github.com/pandulapeter/debug-menu/blob/master/debug-menu-core/src/main/java/com/pandulapeter/debugMenuCore/configuration/UiConfiguration.kt) instance when initializing the library.
* To properly support back navigation, all activities must check if the drawer consumes the event. This is implemented [here](https://github.com/pandulapeter/debug-menu/blob/master/example/src/main/java/com/pandulapeter/debugMenuExample/screens/MainActivity.kt).

### To do
#### Bug fixes
* Fix keyline overlay setting not being synchronised across activities

#### Features
* Create a base class for dialogs with proper 2D scrolling
* Make the expandable list module more generic (single select, multiple select, simple click), add examples (environment switching)
* Add a generic module for displaying a list of custom key-value pairs.
* Add a unique module that displays device / OS information
* Add a generic module for displaying a button.
* Add a unique module for taking a screenshot of the app.
* Add a generic module for switches.
* Add support for writing custom modules?

#### Other
* Add screenshots and a more detailed description to this readme.

### License
```
Copyright 2019 Pandula Péter

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```