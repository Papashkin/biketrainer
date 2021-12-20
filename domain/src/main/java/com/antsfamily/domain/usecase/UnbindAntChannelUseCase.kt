package com.antsfamily.domain.usecase

import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import com.antsfamily.domain.antservice.AntRadioServiceConnection
import javax.inject.Inject

class UnbindAntChannelUseCase @Inject constructor(
    private val connection: AntRadioServiceConnection
) : BaseUseCase<Unit, Result<Unit, Error>>() {
    override suspend fun run(params: Unit): Result<Unit, Error> = try {
        Result.Success(connection.closeBackgroundScanChannel())
    } catch (e: Exception) {
        Result.Failure(Error(e.message ?: "Epic fail :("))
    }
}
