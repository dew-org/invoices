package com.dew.invoices.domain

import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.Clock
import java.time.Instant


@Introspected
data class Invoice(
    @field:BsonProperty("customer") @param:BsonProperty("customer") val customer: Customer,
    @field:BsonProperty("items") @param:BsonProperty("items") val items: List<InvoiceItem>,
) {
    @field:BsonProperty("subTotal")
    val subTotal: Float = items.map { item -> item.subTotal }.reduce { acc, fl -> acc + fl }

    @field:BsonProperty("tax")
    val tax: Float = items.map { item -> item.subTotal * item.tax }.reduce { acc, fl -> acc + fl }

    @field:BsonProperty("discount")
    val discount: Float = items.map { item -> item.subTotal * item.discount }.reduce { acc, fl -> acc + fl }

    @field:BsonProperty
    val total: Float = items.map { item -> item.total }.reduce { acc, fl -> acc + fl }

    @field:BsonProperty("createdAt")
    val createdAt: Instant = Instant.now(Clock.systemUTC())
}
