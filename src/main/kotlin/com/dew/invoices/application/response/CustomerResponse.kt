package com.dew.invoices.application.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class CustomerResponse(val id: String, val fullName: String)
