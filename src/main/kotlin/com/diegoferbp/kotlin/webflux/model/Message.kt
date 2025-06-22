package com.diegoferbp.kotlin.webflux.model

data class Message(
    val id: Long,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)