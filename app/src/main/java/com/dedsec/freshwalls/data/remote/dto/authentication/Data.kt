package com.dedsec.freshwalls.data.remote.dto.authentication

data class Data(
    val email: String,
    val identifier: Int,
    val name: String,
    val role: String,
    val verified: Boolean
)