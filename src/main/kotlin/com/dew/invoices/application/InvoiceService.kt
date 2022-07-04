package com.dew.invoices.application

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.invoices.application.InvoiceMapper.toResponse
import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.application.response.InvoiceResponse
import com.dew.invoices.domain.*
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
class InvoiceService(private val invoiceRepository: InvoiceRepository, private val invoiceProducer: InvoiceProducer) {

    fun save(command: CreateInvoiceCommand): Mono<Boolean> {
        val invoice = Invoice(
            Customer(command.customer.id, command.customer.fullName),
            command.items.map { item ->
                InvoiceItem(
                    Product(item.product.code, item.product.name, item.product.description),
                    item.price,
                    item.quantity,
                    item.tax / 100,
                    item.discount / 100
                )
            },
            command.currency,
            command.userId
        )

        return invoiceRepository.save(invoice).flatMap { added: Boolean ->
            // If a new invoice was added, send product purchased to update stock
            if (added) {
                val purchasedProducts = invoice.items.map {
                    PurchasedProduct(
                        it.product.code, it.product.code, it.quantity
                    )
                }

                invoiceProducer.productPurchase(purchasedProducts)
            }

            Mono.just(added)
        }
    }

    fun searchAll(userId: String): Publisher<InvoiceResponse> {
        return Flux.from(invoiceRepository.searchAll(userId)).map { it.toResponse() }
    }

    fun findById(id: String): Mono<InvoiceResponse> {
        return invoiceRepository.findById(id).mapNotNull { it.toResponse() }
    }
}