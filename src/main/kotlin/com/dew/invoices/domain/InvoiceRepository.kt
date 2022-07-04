package com.dew.invoices.domain

import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import javax.validation.Valid

interface InvoiceRepository {

    fun save(@Valid invoice: Invoice): Mono<Boolean>

    fun searchAll(userId: String): Publisher<Invoice>

    fun findById(id: String): Mono<Invoice>
}