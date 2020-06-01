package com.pandulapeter.beagle.common.contracts

/**
 * Lightweight models representing a single cell to be displayed in the debug menu should implement this interface.
 */
interface ModuleCell<T : ModuleCell<T>> {

    /**
     * Every cell must have a unique ID. The DiffUtil implementation uses this property to check if two items are the same.
     */
    val id: String

    /**
     * This method is called only once to register a [ViewHolderDelegate] implementation specific to the [ModuleCell].
     */
    fun createViewHolderDelegate(): ViewHolderDelegate<T>

    /**
     * To simplify diff calculation, derived classes are encouraged to be data classes.
     */
    override fun equals(other: Any?): Boolean

    /**
     * To simplify diff calculation, derived classes are encouraged to be data classes.
     */
    override fun hashCode(): Int
}