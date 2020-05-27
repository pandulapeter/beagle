# Beagle (Android library)
*A smart and reliable debug menu for Android apps*

<img src="logo.png" width="30%" />

### WARNING
The library is undergoing a complete rewrite with version 2.0.0. Proceed with caution.

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
    def beagleVersion = "1.10.2"
    //def beagleVersion = "2.0.0-alpha01"
    debugImplementation "com.github.pandulapeter.beagle:beagle:$beagleVersion"
    releaseImplementation "com.github.pandulapeter.beagle:beagle-noop:$beagleVersion"
}
```

The latest version is:


[![](https://jitpack.io/v/pandulapeter/beagle.svg)](https://jitpack.io/#pandulapeter/beagle)

The library has to be initialized with an Application instance (preferably in the Application's onCreate() method) by calling:

```kotlin
Beagle.initialize(this)
```
After this a number of modules (tricks) needs to be provided, but this can be changed at any time and the UI will be automatically updated.

By default you can fetch Beagle by shaking the device.

### Tips and tricks
* The appearance of the menu can be personalized by specifying an [Appearance](https://github.com/pandulapeter/beagle/blob/master/beagle-core/src/main/java/com/pandulapeter/beagleCore/configuration/Appearance.kt) instance when initializing the library.
* The behavior of the menu can be personalized by specifying a [Behavior](https://github.com/pandulapeter/beagle/blob/master/beagle-core/src/main/java/com/pandulapeter/beagleCore/configuration/Behavior.kt) instance when initializing the library.

### Changelog
* Check out the [Releases](https://github.com/pandulapeter/beagle/releases) page for the changes in every version. The library uses [semantic versioning](https://semver.org): *MAJOR.MINOR.PATCH* where *PATCH* changes only contain bug fixes, *MINOR* changes add new features and *MAJOR* changes introduce breaking modifications to the API.

### Known issues
* Check out the [Issues](https://github.com/pandulapeter/beagle/issues) page for the list of know problems.

### License
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