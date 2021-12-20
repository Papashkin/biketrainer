package com.antsfamily.domain.usecase

import android.content.Context
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.antservice.AntRadioServiceConnection
import com.dsi.ant.AntService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BindAntChannelUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val connection: AntRadioServiceConnection
) : BaseUseCase<Unit, Boolean>() {

    override suspend fun run(params: Unit): Boolean = AntService.bindService(context, connection)
}

