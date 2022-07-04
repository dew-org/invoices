package com.dew

import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.application.response.InvoiceResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

import javax.validation.Valid

@Client("/invoices")
interface InvoiceClient {

    @Post
    HttpStatus save(@Valid CreateInvoiceCommand command)

    @Get
    List<InvoiceResponse> searchAll(@QueryValue("userId") String userId)

    @Get("/{id}")
    HttpResponse<InvoiceResponse> findById(String id)
}