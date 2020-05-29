# Beagle (Android library)
*A smart and reliable debug menu for Android apps*

<img src="metadata/logo.png" width="30%" />

## WARNING
The library is undergoing a complete rewrite with version 2.0.0. This is not yet ready for public use.
[Click here](https://github.com/pandulapeter/beagle/blob/master/metadata/README_DEPRECATED.md) to see the readme file for the stable version.

## Screenshots
TODO: Coming soon...

## See it in action
Clone this repository, pick a build variant and run the **app** configuration.

## Use it in your project
### Step 1: Add the Jitpack repository
Make sure that the following is part of your project-level build.gradle file:

```groovy
allprojects {
    repositories {
        …
        maven { url "https://jitpack.io" }
    }
}
```

### Step 2: Pick a UI implementation and configure the dependencies
The actual UI of the debug menu can be displayed in multiple ways and this is specified by the suffix of the dependency.
The following versions exist:
* **ui-activity** - Displays the debug menu as a new screen.
* **ui-bottom-sheet** - Displays the debug menu as a modal bottom sheet.
* **ui-dialog** - Displays the debug menu as a modal dialog.
* **ui-drawer** - Displays the debug menu as a side navigation drawer.
* **ui-view** - Displaying the debug menu is the responsibility of the consumer.
* **noop** - No UI, no logic. It has the same public API as all other variants, but it does nothing (this is intended for production builds).

So for example if you prefer the bottom-sheet UI, something like the following needs to be added to your module-level build.gradle file (check the widget below the code snippet for the latest version):

```groovy
dependencies {
    …
    def beagleVersion = "2.0.0-alpha01"
    debugImplementation "com.github.pandulapeter.beagle:ui-bottom-sheet:$beagleVersion"
    releaseImplementation "com.github.pandulapeter.beagle:noop:$beagleVersion"
}
```

The latest version is:


[![](https://jitpack.io/v/pandulapeter/beagle.svg)](https://jitpack.io/#pandulapeter/beagle)

If your project contains multiple modules, you might want to use Beagle in all of them so that you can access its logging capabilities from everywhere. In this case specifying the dependencies in your core module and exposing them through the **api** keyword instead of **implementation** might be a reasonable idea.

### Step 3: Initialize the library
Just one line of code, preferably in the Application's onCreate() method:

```kotlin
Beagle.initialize(this)
```

By default you can fetch Beagle by shaking the device.

### Step 4: Customize by adding modules
After this a number of modules needs to be provided, but this can be changed at any time and the UI will be automatically updated.

TODO: Coming soon...

## Tips and tricks
* The appearance of the menu can be personalized by specifying an [Appearance](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/configuration/Appearance.kt) instance when initializing the library.
* The behavior of the menu can be personalized by specifying a [Behavior](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/configuration/Behavior.kt) instance when initializing the library.

## Documentation
All public functions are documented with Kdoc. The [BeagleContract](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/contracts/BeagleContract.kt) file is a good start for learning about all the built-in capabilities.

If you're interested in what's under the hood, [this document](https://github.com/pandulapeter/beagle/blob/master/metadata/DOCUMENTATION.md) can be helpful while navigating the source code. 

## Changelog
* Check out the [Releases](https://github.com/pandulapeter/beagle/releases) page for the changes in every version. The library uses [semantic versioning](https://semver.org): *MAJOR.MINOR.PATCH* where *PATCH* changes only contain bug fixes, *MINOR* changes add new features and *MAJOR* changes introduce breaking modifications to the API.

## Known issues
* Check out the [Issues](https://github.com/pandulapeter/beagle/issues) page for the list of know problems.
* Don't hesitate to open a new issue if you find a bug or if you have any questions / feature requests!

## License
```
Copyright 2020 Pandula Péter

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