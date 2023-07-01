package me.neversleeps.model

data class Response(
    val statusCode: Int,
    val isBase64Encoded: Boolean,
    val headers: Map<String, String>,
    val body: String,
)
