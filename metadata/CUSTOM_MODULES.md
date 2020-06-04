# Writing custom modules
The built-in modules should cover most use cases, but if you feel like extending them or writing your own module from scratch, Beagle v2 was designed to support this.

One disadvantage to note is that the code written for your custom modules will also be part of your release builds (which is not the case with built-in modules due to the empty **noop** implementation) unless you take special care of it (which might not be worth it). Of course it will never get executed as the *noop* beagle doesn't even do anything with the provided modules, but it is something you should be aware of.

If you come up with a module that seems useful for others, please let me know about it so that we can include it in the library!

# How should I start?
TODO: Coming soon... But meanwhile check out the [Module](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/contracts/module/Module.kt) and [Cell](https://github.com/pandulapeter/beagle/blob/master/common/src/main/java/com/pandulapeter/beagle/common/contracts/module/Cell.kt) interfaces.