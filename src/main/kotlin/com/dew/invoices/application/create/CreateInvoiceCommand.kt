package com.dew.invoices.application.create

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Introspected
data class CreateInvoiceCommand(
    @field:NotBlank @field:NotNull val customer: Customer, @field:NotNull @field:NotEmpty val items: List<InvoiceItem>
)
