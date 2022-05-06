package com.dew.invoices.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.validation.constraints.NotBlank

@Introspected
data class Product @Creator @BsonCreator constructor(
    @field:BsonProperty("code") @param:BsonProperty("code") @field:NotBlank val code: String,
    @field:BsonProperty("name") @param:BsonProperty("name") @field:NotBlank val name: String,
    @field:BsonProperty("description") @param:BsonProperty("description") var description: String?,
) {
    constructor(code: String, name: String) : this(code, name, null)
}
