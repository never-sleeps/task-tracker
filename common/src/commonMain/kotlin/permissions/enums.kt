package me.neversleeps.common.permissions // ktlint-disable filename

enum class AppPrincipalRelations {
    NONE,
    NEW,
    OWN,
    GROUP,
    PUBLIC,
    MODERATABLE,
}

enum class AppUserGroups {
    USER,
    ADMIN_AD,
    MODERATOR_MP,
    TEST,
    BAN_AD,
}

@Suppress("unused")
enum class AppUserPermissions {
    CREATE_OWN,

    READ_OWN,
    READ_GROUP,
    READ_PUBLIC,
    READ_CANDIDATE,

    UPDATE_OWN,
    UPDATE_CANDIDATE,
    UPDATE_PUBLIC,

    DELETE_OWN,
    DELETE_CANDIDATE,
    DELETE_PUBLIC,

    SEARCH_OWN,
    SEARCH_PUBLIC,
    SEARCH_REGISTERED,
    SEARCH_DRAFTS,

    OFFER_FOR_OWN,
}
