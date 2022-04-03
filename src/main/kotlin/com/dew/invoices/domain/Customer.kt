package com.dew.invoices.domain

import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.validation.constraints.NotBlank

@Introspected
data class Customer(
    @field:BsonProperty("id") @param:BsonProperty("id") @field:NotBlank val id: String,
    @field:BsonProperty("fullName") @param:BsonProperty("fullName") @field:NotBlank val fullName: String
)
