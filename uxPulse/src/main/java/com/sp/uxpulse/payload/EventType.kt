package com.sp.uxpulse.payload

sealed class EventType {
    data class Track(val actionType: String) : EventType()
    data object ScreenView : EventType()
    data class State(val actionType: String) : EventType()
}