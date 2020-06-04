# Migrating to version 2.x.x
Things have changed a lot: Beagle has been rewritten from scratch based on your feedback. The old library is still working as you'd expect it to and will be supported for a while but will no longer receive new features. 

## Why should I update?
Lots of exciting reasons!
* Multiple ways to view the debug menu (besides the side drawer, we now have dialog, bottom sheet, full-screen Activity and the library also exposes a View which you can use wherever you want)
* The functionality you're used to is still there, but there are lots of new features too (for example creating Jira tickets directly from Beagle).
* Ability to automatically persist debug settings between app launches.
* Ability to write your own modules.
* Cleaner public API.
* Support for older Android SDK levels (down to 16)
* Much better underlying implementation.

## Why shouldn't I update?
You should eventually! But a few reasons to postpone doing so:
* It will take 10-15 minutes from your day. The new API is designed to be even easier to grasp, but it's considerably different from the old one and you will have to rewrite almost everything related to how you used Beagle.
* The new implementation is not yet battle-tested. I am using it on multiple projects and tried to include all the fixes found in the previous version, but things can go wrong.

## How should I update?
Due to the existence of the legacy implementation, most classes have been moved to a different package and / or renamed in the new version. The simplest way to set up Beagle v2 is to follow the steps written for newcomers in the [main readme](https://github.com/pandulapeter/beagle/blob/master/metadata/README.md).

## That's not a proper migration guide...
You're right! Here are the big changes you should be aware of if you were familiar with the old API:
* You should update the way the library is added in your **build.gradle** file, as mentioned in the readme. The dependencies are a bit different!
* Probably every Beagle-related import you had needs to be manually fixed. The main changes to be aware of:
    * There are no more puns in the API. *Beagle.imprint()* has become **Beagle.initialize()**, and *Trick*s were renamed to **Module**s.
    * Trick (now Module) is no longer a sealed class. Having all of the implementations in the same file was great because you could see all the built-in options using code completion suggestions, but it didn't offer a way for you to write your own modules and was not very scalable. The new way to see all the built-in modules is by going to [this link](https://github.com/pandulapeter/beagle/blob/master/core/src/main/java/com/pandulapeter/beagle/modules/).
* The constructors of the module classes are probably also a bit different. As before, the KDoc headers are the best source of information on which parameter does what.