package com.dew.invoices.application.create

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class InvoiceItem(
    @field:NotBlank @field:NotNull val product: Product,
    @field:NotBlank val price: Float,
    @field:NotBlank val quantity: Int,
    @field:NotBlank val tax: Float,
    @field:NotBlank val discount: Float,
)
