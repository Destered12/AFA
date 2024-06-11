package com.destered.afa_vkr.model

data class HtmlElement(
    val tag: String,
    val text: String,
    val fullText: String,
    val attributes: Map<String, String> = emptyMap(),
)