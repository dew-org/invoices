package com.dew.invoices.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.Clock
import java.time.Instant
import java.util.Date


@Introspected
data class Invoice @Creator @BsonCreator constructor(
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
    val createdAt: Date = Date.from(Instant.now(Clock.systemUTC()))
}
