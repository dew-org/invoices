package com.dew

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.invoices.application.GeneratedInvoice
import com.dew.invoices.application.create.CreateInvoiceCommand
import com.dew.invoices.application.create.Customer
import com.dew.invoices.application.create.InvoiceItem
import com.dew.invoices.application.create.Product
import com.dew.invoices.domain.InvoiceProducer
import io.micronaut.gcp.pubsub.annotation.Topic
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.bson.types.ObjectId
import org.hashids.Hashids
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.lang.Stepwise
import spock.util.concurrent.PollingConditions

import java.util.concurrent.ConcurrentLinkedDeque

@MicronautTest
@Testcontainers
@Stepwise
class InvoiceControllerSpec extends Specification implements TestPropertyProvider {

    private static final Collection<PurchasedProduct> received = new ConcurrentLinkedDeque<>()
    private static final Collection<GeneratedInvoice> receivedInvoices = new ConcurrentLinkedDeque<>()
    private static String invoiceId

    static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse('mongo:5.0.12'))
            .withExposedPorts(27017)

    @Inject
    InvoiceListener listener

    @Inject
    InvoiceClient client

    def 'when save invoice should return CREATED'() {
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
    }

    def 'when find non existing invoice should return NOT_FOUND'() {
        when:
        var id = new Hashids().encodeHex(new ObjectId().toString())
        var response = client.findById(id)

        then:
        response != null
        !response.body.present
    }

    def 'when search invoices by user should return a list of invoices'() {
        when:
        var invoices = client.searchAll("user-01")

        then:
        invoices != null
        !invoices.empty
        invoices.size() == 1

        cleanup:
        invoiceId = invoices[0].id
    }

    def 'when find existing invoice should return an invoice'() {
        when:
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
        mongo.start()

        return ["mongodb.uri": mongo.replicaSetUrl]
    }

    @Singleton
    static class InvoiceListener implements InvoiceProducer {

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
