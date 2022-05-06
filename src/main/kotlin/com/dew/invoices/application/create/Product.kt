package com.dew.invoices.application.create

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class Product(
    @field:NotBlank val code: String,
    @field:NotBlank val name: String,
    var description: String?,
) {
    constructor(code: String, name: String) : this(code, name, null)
}
