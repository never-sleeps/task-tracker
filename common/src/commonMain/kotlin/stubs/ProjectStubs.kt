package ru.otus.otuskotlin.marketplace.common.stubs

enum class ProjectStub {
    NONE,
    SUCCESS,
    NOT_FOUND,
    BAD_ID,
    BAD_TITLE,
    BAD_SEARCH_TEXT,
    BAD_SEARCH_CREATED_BY,
    PERMISSION_ERROR,
}

enum class TaskStub {
    NONE,
    SUCCESS,
    NOT_FOUND,
    BAD_ID,
    BAD_TITLE,
    BAD_SEARCH_CREATED_BY,
    BAD_SEARCH_EXECUTOR,
    BAD_SEARCH_DUE_DATE,
    PERMISSION_ERROR,
}
