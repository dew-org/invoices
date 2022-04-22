package com.dew

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.application.create.Customer
import com.dew.invoices.application.create.InvoiceItem
import com.dew.invoices.application.create.Product
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.awaitility.Awaitility.await
import org.bson.types.ObjectId
import org.hashids.Hashids
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceControllerTest : TestPropertyProvider {

    companion object {
        val received: MutableCollection<PurchasedProduct> = ConcurrentLinkedDeque()
    }

    @Container
    val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))

    @Container
    val mongo: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest")).withExposedPorts(27017)

    @Inject
    lateinit var invoiceListener: InvoiceListener

    @Test
    fun interact_with_invoice_controller(invoiceClient: InvoiceClient) {
        val product = Product("123", "Celular")
        val customer = Customer("321", "Manolo Jesus")
        val invoiceItem = InvoiceItem(product, 15000.0f, 1, 0.0f, 0.0f)
        val invoice = CreateInvoiceCommand(customer, listOf(invoiceItem))

        val status = invoiceClient.save(invoice)

        assertEquals(CREATED, status)

        await().atMost(5, TimeUnit.SECONDS).until {
            !received.isEmpty()
        }

        assertEquals(1, received.size)

        val purchasedFromKafka = received.iterator().next()

        assertNotNull(purchasedFromKafka)
        assertEquals(product.code, purchasedFromKafka.code)
        assertEquals(1, purchasedFromKafka.quantity)

        val response = invoiceClient.searchAll()

        assertEquals(1, response.size)

        var findResponse = invoiceClient.findById(response.first().id)

        assertNotNull(findResponse)
        assertNotNull(findResponse.body())
        assertEquals(HttpStatus.OK, findResponse.status())
        assertEquals(response.first().id, findResponse.body()!!.id)

        val notFountId = Hashids().encodeHex(ObjectId().toString())
        findResponse = invoiceClient.findById(notFountId)
        assertNotNull(findResponse)
        assertEquals(HttpStatus.NOT_FOUND, findResponse.status())
    }

    override fun getProperties(): Map<String, String> {
        kafka.start()
        mongo.start()

        return mapOf(
            "mongodb.uri" to mongo.replicaSetUrl, "kafka.bootstrap.servers" to kafka.bootstrapServers
        )
    }

    @AfterAll
    fun cleanup() {
        received.clear()
    }

    @KafkaListener(offsetReset = OffsetReset.EARLIEST)
    class InvoiceListener {

        @Topic("product-purchase")
        fun productPurchase(products: List<PurchasedProduct>) {
            received.addAll(products)
        }
    }
}