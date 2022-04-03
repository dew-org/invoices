package com.dew.invoices.domain

import reactor.core.publisher.Mono
import javax.validation.Valid

interface InvoiceRepository {

    fun save(@Valid invoice: Invoice): Mono<Boolean>
}