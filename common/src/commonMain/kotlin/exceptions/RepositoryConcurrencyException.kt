package me.neversleeps.common.exceptions

import me.neversleeps.common.models.AppLock

class RepositoryConcurrencyException(expectedLock: AppLock, actualLock: AppLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
