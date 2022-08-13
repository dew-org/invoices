package com.dew.invoices.domain

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.invoices.application.GeneratedInvoice
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface InvoiceProducer {

    @Topic("product-purchase")
    fun productPurchase(products: List<PurchasedProduct>)

    @Topic("invoice-generated")
    fun invoiceGenerated(invoice: GeneratedInvoice)
}