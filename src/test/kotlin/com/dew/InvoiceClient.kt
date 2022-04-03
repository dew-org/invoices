package com.dew

import com.dew.invoices.domain.Invoice
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import javax.validation.Valid

@Client("/invoices")
interface InvoiceClient {

    @Post
    fun save(@Valid invoice: Invoice): HttpStatus
}