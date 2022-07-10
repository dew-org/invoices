package com.dew.invoices.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.time.Clock
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotBlank


@Introspected
@ReflectiveAccess
data class Invoice @Creator @BsonCreator constructor(
    @field:BsonProperty("customer")
    @param:BsonProperty("customer")
    val customer: Customer,

    @field:BsonProperty("items")
    @param:BsonProperty("items")
    val items: List<InvoiceItem>,

    @field:BsonProperty("currency")
    @param:BsonProperty("currency")
    @field:NotBlank
    val currency: String,

    @field:BsonProperty("userId")
    @param:BsonProperty("userId")
    @field:NotBlank
    val userId: String,

    @field:BsonProperty("_id") @param:BsonProperty("_id")
    val id: ObjectId? = null,
) {

    @field:BsonProperty("subtotal")
    var subtotal: Float = 0.0f

    @field:BsonProperty("tax")
    var tax: Float = 0.0f

    @field:BsonProperty("discount")
    var discount: Float = 0.0f

    @field:BsonProperty
    var total: Float = 0.0f

    @field:BsonProperty("createdAt")
    val createdAt: Date = Date.from(Instant.now(Clock.systemUTC()))

    fun calculateTotal() {
        subtotal = items.map { item -> item.subtotal }.reduce { acc, fl -> acc + fl }
        tax = items.map { item -> item.subtotal * item.tax }.reduce { acc, fl -> acc + fl }
        discount = items.map { item -> item.subtotal * item.discount }.reduce { acc, fl -> acc + fl }

        total = items.map { item -> item.total }.reduce { acc, fl -> acc + fl }
    }
}
