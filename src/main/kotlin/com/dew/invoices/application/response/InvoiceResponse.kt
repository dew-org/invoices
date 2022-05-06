package com.dew.invoices.application.response

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class InvoiceResponse(
    val id: String,
    val customer: CustomerResponse,
    val items: List<InvoiceItemResponse>,
    val currency: String,
    val subTotal: Float,
    val tax: Float,
    val discount: Float,
    val total: Float,
    val createdAt: Date
)
