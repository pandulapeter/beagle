package com.pandulapeter.beagleCore

/**
 * Used for specifying the position of a newly added module.
 */
sealed class Positioning {

    /**
     * Adds the new module to the bottom of the list as the last item.
     */
    object Bottom : Positioning()

    /**
     * Adds the new module to the top of the list, as the first item (after the header).
     */
    object Top : Positioning()

    /**
     * Adds the new module below the module with the specified ID, or as the last item if the specified module cannot be found.
     *
     * @param id - The ID of the module which comes before the new module.
     */
    class Below(val id: String) : Positioning()

    /**
     * Adds the new module above the module with the specified ID, or as the first item (after the hedaer) if the specified module cannot be found.
     *
     * @param id - The ID of the module which comes after the new module.
     */
    class Above(val id: String) : Positioning()
}