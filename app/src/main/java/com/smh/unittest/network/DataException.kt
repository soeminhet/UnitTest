package com.smh.unittest.network

sealed class DataException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    data class Network(
        override val message: String = "Unable to connect. Please check connection."
    ): DataException(message = message)

    data class Api(
        override val message: String,
        val title: String = "",
        val errorCode: Int = -1
    ): DataException(message)
}