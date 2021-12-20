package com.antsfamily.data.model

import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult

data class DeviceItem(
    val device: MultiDeviceSearchResult,
    val isSelected: Boolean,
    val isLoading: Boolean
) {
    fun isFitnessEquipment(): Boolean = this.device.antDeviceType == DeviceType.FITNESS_EQUIPMENT
}
