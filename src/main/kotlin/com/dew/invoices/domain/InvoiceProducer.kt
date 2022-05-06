package com.dew.invoices.domain

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import com.dew.common.domain.invoices.PurchasedProduct

@KafkaClient
interface InvoiceProducer {

    @Topic("product-purchase")
    fun productPurchase(products: List<PurchasedProduct>)
}