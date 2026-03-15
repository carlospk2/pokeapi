package com.example.kmm_jcmm_test.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
