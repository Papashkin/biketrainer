package com.antsfamily.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutTimerFlow @Inject constructor() : com.antsfamily.domain.FlowUseCase<Long, Unit>() {

    override fun run(params: Long): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(params)
        }
    }
}
