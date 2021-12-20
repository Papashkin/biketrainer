package com.antsfamily.domain.antservice.channel

interface ChannelChangedListener {
    fun onChannelChanged(newInfo: ChannelInfo)
    fun onAllowStartScan(allowStartScan: Boolean)
}
