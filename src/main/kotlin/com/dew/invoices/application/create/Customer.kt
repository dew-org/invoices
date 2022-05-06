package com.dew.invoices.application.create

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class Customer(
    @field:NotBlank val id: String, @field:NotBlank val fullName: String
)
