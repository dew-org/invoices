package com.dew.invoices.application

import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.domain.*
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Singleton
class InvoiceService(private val invoiceRepository: InvoiceRepository) {

    fun save(command: CreateInvoiceCommand): Mono<Boolean> {
        val invoice = Invoice(
            Customer(command.customer.id, command.customer.fullName),
            command.items.map { item ->
                InvoiceItem(
                    Product(item.product.code, item.product.name, item.product.description),
                    item.price,
                    item.quantity,
                    item.tax,
                    item.discount
                )
            }
        )

        return invoiceRepository.save(invoice)
    }
}