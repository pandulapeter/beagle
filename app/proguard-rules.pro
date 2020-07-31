-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.pandulapeter.beagle.**$$serializer { *; }
-keepclassmembers class com.pandulapeter.beagle.appDemo.data.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.pandulapeter.beagle.appDemo.data.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}