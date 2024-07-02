package com.dedsec.freshwalls.domain.model.authentication

data class User(
    val email: String,
    val identifier: Int,
    val name: String,
    val token: String,
)
