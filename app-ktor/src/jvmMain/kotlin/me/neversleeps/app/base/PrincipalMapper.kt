package me.neversleeps.app.base

import io.ktor.server.auth.jwt.*
import me.neversleeps.app.base.KtorAuthConfig.Companion.F_NAME_CLAIM
import me.neversleeps.app.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import me.neversleeps.app.base.KtorAuthConfig.Companion.ID_CLAIM
import me.neversleeps.app.base.KtorAuthConfig.Companion.L_NAME_CLAIM
import me.neversleeps.app.base.KtorAuthConfig.Companion.M_NAME_CLAIM
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.permissions.AppPrincipalModel
import me.neversleeps.common.permissions.AppUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    AppPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { UserId(it) } ?: UserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> AppUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: AppPrincipalModel.NONE
