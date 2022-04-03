package com.dew.invoices.domain

import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.validation.constraints.NotBlank

@Introspected
data class InvoiceItem(
    @field:BsonProperty("product") @param:BsonProperty("product") @field:NotBlank val product: Product,
    @field:BsonProperty("price") @param:BsonProperty("price") @field:NotBlank val price: Float,
    @field:BsonProperty("amount") @param:BsonProperty("amount") @field:NotBlank val amount: Int,
    @field:BsonProperty("tax") @param:BsonProperty("tax") @field:NotBlank val tax: Float,
    @field:BsonProperty("discount") @param:BsonProperty("discount") @field:NotBlank val discount: Float,
) {
    @field:BsonProperty("subTotal")
    val subTotal: Float = price * amount

    @field:BsonProperty("total")
    val total: Float = (subTotal * (1 + tax)) * (1 - discount)
}
