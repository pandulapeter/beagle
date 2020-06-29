package com.pandulapeter.beagle.common.configuration

/**
 * Used for specifying the position of a newly added module.
 */
sealed class Placement {

    /**
     * Adds the new module to the bottom of the list as the last item.
     */
    object Bottom : Placement()

    /**
     * Adds the new module to the top of the list, as the first item (after the header).
     */
    object Top : Placement()

    /**
     * Adds the new module below the module with the specified ID, or as the last item if the specified module cannot be found.
     *
     * @param id - The ID of the module which comes before the new module.
     */
    class Below(val id: String) : Placement()

    /**
     * Adds the new module above the module with the specified ID, or as the first item if the specified module cannot be found.
     *
     * @param id - The ID of the module which comes after the new module.
     */
    class Above(val id: String) : Placement()
}