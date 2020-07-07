package com.pandulapeter.beagle.core.manager

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class LocalStorageManager(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences("beagle", Context.MODE_PRIVATE)
    val booelans by PersistedProperty.Boolean("boolean_")
    val integers by PersistedProperty.Integer("integer_")
    val strings by PersistedProperty.String("string_")
    val stringSets by PersistedProperty.StringSet("stringSet_")

    sealed class PersistedProperty<T>(private val mainKey: kotlin.String) : ReadOnlyProperty<LocalStorageManager, SharedPreferencesMap<T?>> {

        private var map: SharedPreferencesMap<T?>? = null

        override fun getValue(thisRef: LocalStorageManager, property: KProperty<*>) = map ?: createSharedPreferencesMap(thisRef.preferences, mainKey).also { map = it }

        abstract fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String): SharedPreferencesMap<T?>

        class Boolean(mainKey: kotlin.String) : PersistedProperty<kotlin.Boolean>(mainKey) {

            override fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String) = SharedPreferencesMap.Boolean(preferences, mainKey)
        }

        class Integer(mainKey: kotlin.String) : PersistedProperty<Int>(mainKey) {

            override fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String) = SharedPreferencesMap.Integer(preferences, mainKey)
        }

        class String(mainKey: kotlin.String) : PersistedProperty<kotlin.String>(mainKey) {

            override fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String) = SharedPreferencesMap.String(preferences, mainKey)
        }

        class StringSet(mainKey: kotlin.String) : PersistedProperty<Set<kotlin.String>>(mainKey) {

            override fun createSharedPreferencesMap(preferences: SharedPreferences, mainKey: kotlin.String) = SharedPreferencesMap.StringSet(preferences, mainKey)
        }
    }

    sealed class SharedPreferencesMap<T>(protected val preferences: SharedPreferences, private val mainKey: kotlin.String) {

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

        class Integer(preferences: SharedPreferences, mainKey: kotlin.String) : SharedPreferencesMap<Int?>(preferences, mainKey) {

            override fun getFinal(key: kotlin.String) = if (preferences.contains(key)) preferences.getInt(key, 0) else null

            override fun setFinal(key: kotlin.String, value: Int?) = preferences.edit().putInt(key, value ?: 0).apply()
        }

        class String(preferences: SharedPreferences, mainKey: kotlin.String) : SharedPreferencesMap<kotlin.String?>(preferences, mainKey) {

            override fun getFinal(key: kotlin.String) = preferences.getString(key, null)

            override fun setFinal(key: kotlin.String, value: kotlin.String?) = preferences.edit().putString(key, value).apply()
        }

        class StringSet(preferences: SharedPreferences, mainKey: kotlin.String) : SharedPreferencesMap<Set<kotlin.String>?>(preferences, mainKey) {

            override fun getFinal(key: kotlin.String) = preferences.getStringSet(key, null).orEmpty()

            override fun setFinal(key: kotlin.String, value: Set<kotlin.String>?) = preferences.edit().putStringSet(key, value).apply()
        }
    }
}