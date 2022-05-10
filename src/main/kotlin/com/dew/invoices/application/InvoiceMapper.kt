package com.dew.invoices.application

import com.dew.invoices.application.response.CustomerResponse
import com.dew.invoices.application.response.InvoiceItemResponse
import com.dew.invoices.application.response.InvoiceResponse
import com.dew.invoices.application.response.ProductResponse
import com.dew.invoices.domain.Customer
import com.dew.invoices.domain.Invoice
import com.dew.invoices.domain.InvoiceItem
import com.dew.invoices.domain.Product
import org.hashids.Hashids

object InvoiceMapper {

    fun Invoice.toResponse(): InvoiceResponse {
        val hashids = Hashids()

        return InvoiceResponse(
            hashids.encodeHex(id.toString()),
            customer.toResponse(),
            items.toResponse(),
            currency,
            subtotal,
            tax,
            discount,
            total,
            createdAt
        )
    }

    private fun Customer.toResponse(): CustomerResponse {
        return CustomerResponse(
            id, fullName
        )
    }

    private fun Product.toResponse(): ProductResponse {
        return ProductResponse(
            code, name, description
        )
    }

    private fun List<InvoiceItem>.toResponse(): List<InvoiceItemResponse> {
        return map { item ->
            InvoiceItemResponse(
                item.product.toResponse(), item.price, item.quantity, item.tax, item.discount, item.subtotal, item.total
            )
        }
    }
}