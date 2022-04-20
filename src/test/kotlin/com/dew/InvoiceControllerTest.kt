package com.dew

import com.dew.common.infrastructure.persistence.mongo.testing.MongoDbUtils
import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.application.create.Customer
import com.dew.invoices.application.create.InvoiceItem
import com.dew.invoices.application.create.Product
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceControllerTest : TestPropertyProvider {

    @Test
    fun save_invoice_should_return_ok(invoiceClient: InvoiceClient) {
        val product = Product("123", "Celular")
        val customer = Customer("321", "Manolo Jesus")
        val invoiceItem = InvoiceItem(product, 15000.0f, 1, 0.0f, 0.0f)
        val invoice = CreateInvoiceCommand(customer, listOf(invoiceItem))

        val status = invoiceClient.save(invoice)

        assertEquals(CREATED, status)
    }

    override fun getProperties(): Map<String, String> {
        MongoDbUtils.startMongoDb()
        return mapOf("mongodb.uri" to MongoDbUtils.mongoDbUri)
    }

    @AfterAll
    fun cleanup() {
        MongoDbUtils.closeMongoDb()
    }
}