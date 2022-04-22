package com.dew.invoices.application

import com.dew.invoices.application.response.CustomerResponse
import com.dew.invoices.application.response.InvoiceItemResponse
import com.dew.invoices.application.response.InvoiceResponse
import com.dew.invoices.application.response.ProductResponse
import com.dew.invoices.domain.Customer
import com.dew.invoices.domain.Invoice
import com.dew.invoices.domain.InvoiceItem
import com.dew.invoices.domain.Product

object InvoiceMapper {

    fun Invoice.toResponse(): InvoiceResponse {
        return InvoiceResponse(
            this.customer.toResponse(),
            this.items.toResponse(),
            this.subTotal,
            this.tax,
            this.discount,
            this.total,
            this.createdAt
        )
    }

    private fun Customer.toResponse(): CustomerResponse {
        return CustomerResponse(
            this.id, this.fullName
        )
    }

    private fun Product.toResponse(): ProductResponse {
        return ProductResponse(
            this.code, this.name, this.description
        )
    }

    private fun List<InvoiceItem>.toResponse(): List<InvoiceItemResponse> {
        return this.map { item ->
            InvoiceItemResponse(
                item.product.toResponse(),
                item.price,
                item.quantity,
                item.tax,
                item.discount,
                item.subTotal,
                item.total
            )
        }
    }
}