package com.actia.myapplication.util

import androidx.test.espresso.idling.CountingIdlingResource

object IdlingResourceCounter {
    private const val IDLING_RESOURCE_NAME = "GlobalIdlingResourceCounter"
    var countingIdlingResource = CountingIdlingResource(IDLING_RESOURCE_NAME)

    fun reset() {
        countingIdlingResource = CountingIdlingResource(IDLING_RESOURCE_NAME)
    }
}