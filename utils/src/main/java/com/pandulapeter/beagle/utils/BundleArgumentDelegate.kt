package com.pandulapeter.beagle.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class BundleArgumentDelegate<T>(protected val key: kotlin.String, protected val defaultValue: T) : ReadWriteProperty<Bundle?, T> {

    class Boolean(key: kotlin.String, defaultValue: kotlin.Boolean = false) : BundleArgumentDelegate<kotlin.Boolean>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getBoolean(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.Boolean) = thisRef?.putBoolean(key, value) ?: Unit
    }

    class Long(key: kotlin.String, defaultValue: kotlin.Long = 0L) : BundleArgumentDelegate<kotlin.Long>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getLong(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.Long) = thisRef?.putLong(key, value) ?: Unit
    }

    class String(key: kotlin.String, defaultValue: kotlin.String = "") : BundleArgumentDelegate<kotlin.String>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getString(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.String) = thisRef?.putString(key, value) ?: Unit
    }

    class StringList(key: kotlin.String, defaultValue: List<kotlin.String?> = emptyList()) : BundleArgumentDelegate<List<kotlin.String?>>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getStringArrayList(key) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: List<kotlin.String?>) = thisRef?.putStringArrayList(key, ArrayList(value)) ?: Unit
    }

    class CharSequence(key: kotlin.String, defaultValue: kotlin.CharSequence = "") : BundleArgumentDelegate<kotlin.CharSequence>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getCharSequence(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.CharSequence) = thisRef?.putCharSequence(key, value) ?: Unit
    }
}