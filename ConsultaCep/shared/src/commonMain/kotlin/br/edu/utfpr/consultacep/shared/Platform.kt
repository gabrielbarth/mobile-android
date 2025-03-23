package br.edu.utfpr.consultacep.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform