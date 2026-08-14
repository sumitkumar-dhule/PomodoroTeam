package com.example.pomodoroteam

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform