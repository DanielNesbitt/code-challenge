package com.genedata.session

import com.genedata.models.DB
import io.ktor.application.ApplicationCall
import io.ktor.auth.Principal
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.UserPasswordCredential
import org.mindrot.jbcrypt.BCrypt

/**
 * @author Daniel Nesbitt
 */
val NUM_ROUNDS = 8

fun createValidator(db: DB): suspend ApplicationCall.(UserPasswordCredential) -> Principal? {
    return {
        val user = db.getUser(it.name)
        if (user != null && user.validatePassword(it.password))
            UserIdPrincipal(it.name)
        else null
    }
}

fun hashPassword(pass: String): String {
    val salt = BCrypt.gensalt(NUM_ROUNDS)
    return BCrypt.hashpw(pass, salt)
}
