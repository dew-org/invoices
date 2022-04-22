package com.dew.invoices.application.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class InvoiceItemResponse(
    val product: ProductResponse,
    val price: Float,
    val quantity: Int,
    val tax: Float,
    val discount: Float,
    val subTotal: Float,
    val total: Float
)
