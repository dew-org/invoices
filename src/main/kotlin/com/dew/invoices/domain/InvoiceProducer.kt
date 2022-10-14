package com.dew.invoices.domain

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.invoices.application.GeneratedInvoice
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.gcp.pubsub.annotation.PubSubClient
import io.micronaut.gcp.pubsub.annotation.Topic

@PubSubClient
@Requires(notEnv = [Environment.TEST])
interface InvoiceProducer {

    @Topic("product-purchase")
    fun productPurchase(products: List<PurchasedProduct>)

    @Topic("invoice-generated")
    fun invoiceGenerated(invoice: GeneratedInvoice)
}