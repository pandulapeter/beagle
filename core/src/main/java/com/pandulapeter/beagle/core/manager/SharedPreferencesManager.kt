package com.pandulapeter.beagle.core.manager

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class SharedPreferencesManager(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences("beagle", Context.MODE_PRIVATE)

    private sealed class PersistedProperty<T>(private val mainKey: kotlin.String) : ReadOnlyProperty<SharedPreferencesManager, SharedPreferencesMap<T?>> {

        private var map: SharedPreferencesMap<T?>? = null

        override fun getValue(thisRef: SharedPreferencesManager, property: KProperty<*>) = map ?: createSharedPreferencesMap(thisRef.preferences, mainKey).also { map = it }

        abstract fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String): SharedPreferencesMap<T?>

        class Boolean(mainKey: kotlin.String) : PersistedProperty<kotlin.Boolean>(mainKey) {

            override fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String) = SharedPreferencesMap.Boolean(preferences, mainKey)
        }

        class String(mainKey: kotlin.String) : PersistedProperty<kotlin.String>(mainKey) {

            override fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String) = SharedPreferencesMap.String(preferences, mainKey)
        }
    }

    private sealed class SharedPreferencesMap<T>(protected val preferences: SharedPreferences, private val mainKey: kotlin.String) {

        operator fun get(key: kotlin.String) = getFinal(mainKey + key)

        operator fun set(key: kotlin.String, value: T) {
            if (value == null) {
                preferences.edit().remove(mainKey + value).apply()
            } else {
                setFinal(mainKey + key, value)
            }
        }

        abstract fun getFinal(key: kotlin.String): T

        abstract fun setFinal(key: kotlin.String, value: T)

        class Boolean(preferences: SharedPreferences, mainKey: kotlin.String) : SharedPreferencesMap<kotlin.Boolean?>(preferences, mainKey) {

            override fun getFinal(key: kotlin.String) = if (preferences.contains(key)) preferences.getBoolean(key, false) else null

            override fun setFinal(key: kotlin.String, value: kotlin.Boolean?) = preferences.edit().putBoolean(key, value == true).apply()
        }

        class String(preferences: SharedPreferences, mainKey: kotlin.String) : SharedPreferencesMap<kotlin.String?>(preferences, mainKey) {

            override fun getFinal(key: kotlin.String) = preferences.getString(key, null)

            override fun setFinal(key: kotlin.String, value: kotlin.String?) = preferences.edit().putString(key, value).apply()
        }
    }
}