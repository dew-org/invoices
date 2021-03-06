package com.dew.invoices.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.validation.constraints.NotBlank

@Introspected
@ReflectiveAccess
data class InvoiceItem @Creator @BsonCreator constructor(
    @field:BsonProperty("product")
    @param:BsonProperty("product")
    @field:NotBlank
    val product: Product,

    @field:BsonProperty("price")
    @param:BsonProperty("price")
    @field:NotBlank val price: Float,

    @field:BsonProperty("quantity")
    @param:BsonProperty("quantity")
    @field:NotBlank val quantity: Int,

    @field:BsonProperty("tax")
    @param:BsonProperty("tax")
    @field:NotBlank val tax: Float,

    @field:BsonProperty("discount")
    @param:BsonProperty("discount")
    @field:NotBlank val discount: Float,
) {
    @field:BsonProperty("subtotal")
    val subtotal: Float = price * quantity

    @field:BsonProperty("total")
    val total: Float = (subtotal * (1 + tax)) * (1 - discount)
}
