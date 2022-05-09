package com.dew.invoices.infrastructure.persistence.mongo

import com.dew.invoices.domain.*
import com.dew.invoices.infrastructure.persistence.mongo.MongoDbMapping.toDocument
import com.dew.invoices.infrastructure.persistence.mongo.MongoDbMapping.toInvoice
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.bson.Document
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
open class MongoDbInvoiceRepository(
    private val mongoDbConfiguration: MongoDbConfiguration, private val mongoClient: MongoClient
) : InvoiceRepository {

    override fun save(invoice: Invoice): Mono<Boolean> =
        Mono.from(collection.insertOne(invoice.toDocument()))
            .map { true }.onErrorReturn(false)

    override fun searchAll(): Publisher<Invoice> =
        Flux.from(collection.find()).map { it.toInvoice() }

    override fun findById(id: String): Mono<Invoice> = Mono.from(
        collection.find(Filters.eq("_id", ObjectId(id))).first()
    ).mapNotNull { it.toInvoice() }

    private val collection: MongoCollection<Document>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection)
}