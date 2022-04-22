package com.dew.invoices.application.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class ProductResponse(
    val code: String, val name: String, val description: String?
)
