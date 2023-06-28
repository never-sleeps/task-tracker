package me.neversleeps.common.models

enum class AppCommand {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    SEARCH,
}

enum class AppState {
    NONE,
    RUNNING,
    FAILING,
}
