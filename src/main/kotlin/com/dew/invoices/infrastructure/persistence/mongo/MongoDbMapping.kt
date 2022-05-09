package com.dew.invoices.infrastructure.persistence.mongo

import com.dew.invoices.domain.Customer
import com.dew.invoices.domain.Invoice
import com.dew.invoices.domain.InvoiceItem
import com.dew.invoices.domain.Product
import org.bson.Document
import org.bson.types.ObjectId

object MongoDbMapping {

    fun Invoice.toDocument(): Document = Document()
        .append("customer", customer.toDocument())
        .append("items", items.map { it.toDocument() })
        .append("currency", currency)
        .append("subtotal", subtotal)
        .append("tax", tax)
        .append("discount", discount)
        .append("total", total)
        .append("createdAt", createdAt)

    fun Customer.toDocument(): Document = Document()
        .append("id", id)
        .append("fullName", fullName)


    fun InvoiceItem.toDocument(): Document = Document()
        .append("product", product.toDocument())
        .append("price", price)
        .append("quantity", quantity)
        .append("tax", tax)
        .append("discount", discount)
        .append("subtotal", subtotal)
        .append("total", total)

    fun Product.toDocument(): Document = Document()
        .append("code", code)
        .append("name", name)
        .append("description", description)

    fun Document.toInvoice(): Invoice = Invoice(
        id = get("_id", ObjectId::class.java),
        customer = get("customer", Document::class.java).toCustomer(),
        items = getList("items", Document::class.java).map { it.toInvoiceItem() },
        currency = getString("currency")
    )

    private fun Document.toCustomer(): Customer = Customer(
        id = getString("id"),
        fullName = getString("fullName")
    )

    private fun Document.toInvoiceItem(): InvoiceItem = InvoiceItem(
        product = get("product", Document::class.java).toProduct(),
        price = getDouble("price").toFloat(),
        quantity = getInteger("quantity"),
        tax = getDouble("tax").toFloat(),
        discount = getDouble("discount").toFloat()
    )

    private fun Document.toProduct(): Product = Product(
        code = getString("code"),
        name = getString("name"),
        description = getString("description")
    )
}