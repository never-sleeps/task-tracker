package me.neversleeps.acceptance.blackbox.test.action.v1.utils // ktlint-disable filename

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.and
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse
import me.neversleeps.api.jackson.v1.models.ProjectDeleteResponse
import me.neversleeps.api.jackson.v1.models.ProjectReadResponse
import me.neversleeps.api.jackson.v1.models.ProjectResponseObject
import me.neversleeps.api.jackson.v1.models.ProjectSearchResponse
import me.neversleeps.api.jackson.v1.models.ProjectUpdateResponse
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus

fun haveResult(result: ResponseResultStatus) = Matcher<IResponse> {
    MatcherResult(
        it.resultStatus == result,
        { "actual result ${it.resultStatus} but we expected $result" },
        { "result should not be $result" },
    )
}

val haveNoErrors = Matcher<IResponse> {
    MatcherResult(
        it.errors.isNullOrEmpty(),
        { "actual errors ${it.errors} but we expected no errors" },
        { "errors should not be empty" },
    )
}

fun haveError(code: String) = haveResult(ResponseResultStatus.ERROR)
    .and(
        Matcher<IResponse> {
            MatcherResult(
                it.errors?.firstOrNull { e -> e.code == code } != null,
                { "actual errors ${it.errors} but we expected error with code $code" },
                { "errors should not contain $code" },
            )
        },
    )

val haveSuccessResult = haveResult(ResponseResultStatus.SUCCESS) and haveNoErrors

val IResponse.project: ProjectResponseObject?
    get() = when (this) {
        is ProjectCreateResponse -> project
        is ProjectReadResponse -> project
        is ProjectUpdateResponse -> project
        is ProjectDeleteResponse -> project
        is ProjectSearchResponse -> projects?.get(0)
        else -> throw IllegalArgumentException("Invalid response type: ${this::class}")
    }
