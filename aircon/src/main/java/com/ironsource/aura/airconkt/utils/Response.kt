package com.ironsource.aura.airconkt.utils

sealed class Response<T, E : Throwable>

class Success<T, E : Throwable>(val value: T) : Response<T, E>()
class Fail<T, E : Throwable>(val exception: E) : Response<T, E>()