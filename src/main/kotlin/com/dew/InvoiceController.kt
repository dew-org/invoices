package com.dew

import com.dew.invoices.application.response.InvoiceResponse
import com.dew.invoices.application.InvoiceService
import com.dew.invoices.application.create.CreateInvoiceCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.hashids.Hashids
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/invoices")
@Secured(SecurityRule.IS_AUTHENTICATED)
open class InvoiceController(private val invoiceService: InvoiceService) {

    @Post
    open fun save(@Valid command: CreateInvoiceCommand): Mono<HttpStatus> {
        return invoiceService.save(command)
            .map { added: Boolean -> if (added) HttpStatus.CREATED else HttpStatus.CONFLICT }
    }

    @Get
    open fun searchAll(): Publisher<InvoiceResponse> = invoiceService.searchAll()

    @Get("/{id}")
    open fun findById(id: String): Mono<HttpResponse<InvoiceResponse>> {
        val hashids = Hashids()
        val decoded = hashids.decodeHex(id)

        return invoiceService.findById(decoded).map { invoice: InvoiceResponse? ->
            if (invoice != null) HttpResponse.ok(invoice) else HttpResponse.notFound()
        }
    }
}