package com.antsfamily.domain.antservice.channel

interface ChannelBroadcastListener {
    fun onBroadcastChanged(newInfo: ChannelInfo)
    fun onBackgroundScanStateChange(backgroundScanInProgress: Boolean, backgroundScanIsConfigured: Boolean)
    fun onChannelDeath()
}
