@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.genedata.ws

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor

/**
 * @author Daniel Nesbitt
 */
sealed class ConnectionMsg

fun CoroutineScope.connectionActor() = actor<ConnectionMsg> {

    for (msg in channel) {
        when (msg) {
        }
    }

}
