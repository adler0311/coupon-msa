package com.example.couponmsa.domain

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = DiscountTypeDeserializer::class)
enum class DiscountType(val code: String) {
    RATE("rate"),
    VALUE("value");

}

class DiscountTypeDeserializer : JsonDeserializer<DiscountType>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): DiscountType {
        val code = p.text
        return DiscountType.values().firstOrNull { it.code.equals(code, ignoreCase = true) }
            ?: throw IllegalArgumentException("Unknown code: $code")
    }
}