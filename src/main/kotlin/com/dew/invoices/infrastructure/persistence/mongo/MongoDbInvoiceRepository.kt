package com.dew.invoices.infrastructure.persistence.mongo

import com.dew.invoices.domain.Invoice
import com.dew.invoices.domain.InvoiceRepository
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
open class MongoDbInvoiceRepository(
    private val mongoDbConfiguration: MongoDbConfiguration, private val mongoClient: MongoClient
) : InvoiceRepository {

    override fun save(invoice: Invoice): Mono<Boolean> =
        Mono.from(collection.insertOne(invoice))
            .map { true }.onErrorReturn(false)

    override fun searchAll(userId: String): Publisher<Invoice> =
        Flux.from(collection.find(Filters.eq("userId", userId)))

    override fun findById(id: String): Mono<Invoice> = Mono.from(
        collection.find(Filters.eq("_id", ObjectId(id))).first()
    )

    private val collection: MongoCollection<Invoice>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection, Invoice::class.java)
}