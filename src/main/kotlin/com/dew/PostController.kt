package com.dew

import com.dew.invoices.domain.Invoice
import com.dew.invoices.domain.InvoiceRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.CONFLICT
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/invoices")
open class PostController(private val invoiceRepository: InvoiceRepository) {

    @Post
    open fun save(@Valid invoice: Invoice): Mono<HttpStatus> {
        return invoiceRepository.save(invoice).map { added: Boolean -> if (added) CREATED else CONFLICT }
    }
}