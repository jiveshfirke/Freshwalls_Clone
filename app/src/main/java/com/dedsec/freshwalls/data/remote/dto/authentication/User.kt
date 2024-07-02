package com.dedsec.freshwalls.data.remote.dto.authentication

data class User(
    val exception: Any,
    val headers: Headers,
    val original: Original
)