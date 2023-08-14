package me.neversleeps.common

import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repositoryStub: IProjectRepository = IProjectRepository.NONE,
    val repositoryTest: IProjectRepository = IProjectRepository.NONE,
    val repositoryProd: IProjectRepository = IProjectRepository.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
