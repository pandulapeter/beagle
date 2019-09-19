package com.pandulapeter.debugMenu.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal sealed class BundleArgumentDelegate<T>(protected val key: String, protected val defaultValue: T) : ReadWriteProperty<Bundle?, T> {

    class Boolean(key: String, defaultValue: kotlin.Boolean = false) : BundleArgumentDelegate<kotlin.Boolean>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getBoolean(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.Boolean) = thisRef?.putBoolean(key, value) ?: Unit
    }

    class Parcelable<P : android.os.Parcelable>(key: String, defaultValue: P? = null) : BundleArgumentDelegate<P?>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getParcelable(key) as P?

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: P?) = thisRef?.putParcelable(key, value) ?: Unit
    }
}