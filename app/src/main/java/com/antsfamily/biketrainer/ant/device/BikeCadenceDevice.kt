package com.antsfamily.biketrainer.ant.device

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.dsi.ant.plugins.antplus.pcc.AntPlusBikeCadencePcc
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.math.BigDecimal
import javax.inject.Inject

class BikeCadenceDevice @Inject constructor(@ApplicationContext private val context: Context) {

    private var _cadence: AntPlusBikeCadencePcc? = null

    fun getAccess(
        deviceNumber: Int,
        resultReceivedCallback: (result: RequestAccessResult) -> Unit
    ) {
        AntPlusBikeCadencePcc.requestAccess(
            context,
            deviceNumber,
            SEARCH_PROXIMITY_THRESHOLD,
            false,
            /* Handle the result, connecting to events on success or reporting failure to user. */
            { result, resultCode, _ ->
                if (resultCode == RequestAccessResult.SUCCESS) {
                    _cadence = result
                }
                resultReceivedCallback(resultCode)
            },
            { state ->
                if (state == DeviceState.DEAD) {
                    _cadence = null
                }
            }
        )
    }

    /**
     * Subscribe to all the heart rate events, connecting them to display their data.
     */
    private fun subscribe(cadenceCallback: (cadence: BigDecimal) -> Unit) {
        _cadence?.let {
            it.subscribeCalculatedCadenceEvent { _, _, cadence ->
                Handler(Looper.getMainLooper()).post { cadenceCallback(cadence) }
            }

            it.subscribeRawCadenceDataEvent { _, _, _, _ ->
                Handler(Looper.getMainLooper()).post {}
            }
        }
    }

    fun clear() {
        _cadence = null
    }

    companion object {
        private const val SEARCH_PROXIMITY_THRESHOLD = 0
    }
}
