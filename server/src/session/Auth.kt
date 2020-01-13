package com.genedata.session

import io.ktor.application.ApplicationCall
import io.ktor.auth.Principal
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.UserPasswordCredential

/**
 * @author Daniel Nesbitt
 */
const val AUTH_PROVIDER = "BasicAuthProvider"

val validator: suspend ApplicationCall.(UserPasswordCredential) -> Principal? = {
    if (it.name == "test" && it.password == "password")
        UserIdPrincipal(it.name)
    else null
}
