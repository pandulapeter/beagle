# Beagle (Android library)
*A smart, reliable, and highly customizable debug menu for Android apps*

<img src="metadata/logo.png" width="20%" />

**WARNING! The library is undergoing a complete rewrite with version 2.0.0. This is not yet ready for public use. Even this document contains references to unimplemented features.**

**[Click here](https://github.com/pandulapeter/beagle/blob/master/metadata/README_DEPRECATED.md) to see the readme file for the stable version.**

## See it in action
Clone this repository, pick a build variant and run the **app** configuration. It should look something like this:

<img src="metadata/screenshotOld01.png" width="25%" /><img src="metadata/screenshotOld02.png" width="25%" /><img src="metadata/screenshotOld01.png" width="25%" /><img src="metadata/screenshotOld02.png" width="25%" />

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
    def beagleVersion = "2.0.0-alpha05"
    debugImplementation "com.github.pandulapeter.beagle:ui-bottom-sheet:$beagleVersion"
    releaseImplementation "com.github.pandulapeter.beagle:noop:$beagleVersion"
}
```

The latest version is:


[![](https://jitpack.io/v/pandulapeter/beagle.svg)](https://jitpack.io/#pandulapeter/beagle)

**Note 1**: In case of the drawer UI, if you have overwritten the Activity's onBackPressed() method, you might notice that the default back navigation handling does not always work as expected. To fix this, in every Activity's onBackPressed() you should check that Beagle.hide() returns false before doing any other checks or calling the super implementation.

**Note 2**: If your project contains multiple modules, you might want to use Beagle in all of them so that you can access its logging capabilities from everywhere. In this case specifying the dependencies in your core Android module and exposing them through the **api** keyword instead of **implementation** might be a reasonable idea.

### Step 3: Initialize the library
Just one line of code, preferably in the Application's onCreate() method:

```kotlin
Beagle.initialize(this)
```

Optionally you can add the following parameters to this function:
* The appearance of the menu can be personalized by specifying an [Appearance](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/configuration/Appearance.kt) instance.
* The behavior of the menu can be personalized by specifying a [Behavior](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/configuration/Behavior.kt) instance.

By default you can fetch Beagle by shaking the device.

### Step 4: Finish the setup by adding modules
After this a number of modules should be provided, but this configuration can be changed at any time (from any thread) and the UI will automatically be updated. The simplest way of doing this is by calling:

```kotlin
Beagle.setModules(module1, module2, ...)
```
At this point you should be aware of two options:
* The list of [built-in modules](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/modules/). Every file in this package is documented. These modules should cover most use cases and have the advantage of also providing a fake, **noop** implementation which means that no part of their logic is compiled into your release builds.
* The ability to write custom modules. For this a good starting point is looking at the built-in implementations from above, but [this document](https://github.com/pandulapeter/beagle/blob/master/metadata/CUSTOM_MODULES.md) also provides some guidance.

Check out [this implementation](https://github.com/pandulapeter/beagle/blob/master/app/src/main/java/com/pandulapeter/beagle/appExample/BeagleExampleApplication.kt) for some ideas on what is possible with the built-in modules.

## Documentation
All public functions are documented with KDoc. The [BeagleContract](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/contracts/BeagleContract.kt) file is a good start for learning about all the built-in capabilities. For information on the individual modules, see the relevant class headers, as mentioned before.

If you're interested in what's under the hood, [this document](https://github.com/pandulapeter/beagle/blob/master/metadata/DOCUMENTATION.md) can be helpful while navigating the source code. 

## Changelog
Check out the [Releases](https://github.com/pandulapeter/beagle/releases) page for the changes in every version.

If you're updating from a version older than 2.x.x, check out this [migration guide](https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md).

The library uses [semantic versioning](https://semver.org): **MAJOR.MINOR.PATCH** where **PATCH** changes only contain bug fixes, **MINOR** changes add new features and **MAJOR** changes introduce breaking modifications to the API.

## Known issues
Check out the [Issues](https://github.com/pandulapeter/beagle/issues) page for the list of know problems.

Don't hesitate to open a new issue if you find a bug or if you have any questions / feature requests!

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