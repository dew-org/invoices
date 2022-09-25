package com.dew

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.invoices.application.GeneratedInvoice
import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.application.create.Customer
import com.dew.invoices.application.create.InvoiceItem
import com.dew.invoices.application.create.Product
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.bson.types.ObjectId
import org.hashids.Hashids
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.util.concurrent.ConcurrentLinkedDeque

@MicronautTest
@Testcontainers
class InvoiceControllerSpec extends Specification implements TestPropertyProvider {

    private static final Collection<PurchasedProduct> received = new ConcurrentLinkedDeque<>()
    private static final Collection<GeneratedInvoice> receivedInvoices = new ConcurrentLinkedDeque<>()

    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse('confluentinc/cp-kafka:latest'))

    static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse('mongo:5.0.12'))
            .withExposedPorts(27017)

    @Inject
    InvoiceListener listener

    @Inject
    InvoiceClient client

    def 'test interact with invoices controller'() {
        when:
        var product = new Product("123", "Celular")
        var customer = new Customer("123", "Joao")
        var invoiceItem = new InvoiceItem(product, 10000.0f, 1, 0.0f, 0.1f)
        var invoice = new CreateInvoiceCommand(customer, List.of(invoiceItem), "USD", "user-01")

        var status = client.save(invoice)

        then:
        status == HttpStatus.CREATED

        new PollingConditions(timeout: 5).eventually {
            !received.isEmpty()
            1 == received.size()
            1 == receivedInvoices.size()
        }

        when:
        var id = new Hashids().encodeHex(new ObjectId().toString())
        var response = client.findById(id)

        then:
        response != null
        !response.body.present

        when:
        var invoices = client.searchAll("user-02")

        then:
        invoices != null
        invoices.empty

        when:
        invoices = client.searchAll("user-01")

        then:
        invoices != null
        !invoices.empty

        when:
        var invoiceId = invoices[0].id
        var findResponse = client.findById(invoiceId)

        then:
        findResponse != null
        findResponse.body.present
        findResponse.body().id == invoiceId
        findResponse.body().currency == "USD"
        findResponse.body().subtotal == 10000.0f
        findResponse.body().tax == 0.0f
        findResponse.body().discount == 1000.0f
        findResponse.body().total == 9000.0f
    }

    @Override
    Map<String, String> getProperties() {
        kafka.start()
        mongo.start()

        return ["kafka.bootstrap.servers": kafka.bootstrapServers,
                "mongodb.uri"            : mongo.replicaSetUrl]
    }

    @KafkaListener(offsetReset = OffsetReset.EARLIEST)
    static class InvoiceListener {

        @Topic('product-purchase')
        void productPurchase(List<PurchasedProduct> products) {
            received.addAll(products)
        }

        @Topic("invoice-generated")
        void invoiceGenerated(GeneratedInvoice invoice) {
            receivedInvoices.add(invoice)
        }
    }
}
