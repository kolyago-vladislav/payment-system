package com.example.transaction.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BigDecimalFromObjectDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        int scale = node.get("scale").asInt();
        String base64 = node.get("value").asText();
        byte[] decoded = Base64.getDecoder().decode(base64);
        BigInteger unscaled = new BigInteger(decoded);

        return new BigDecimal(unscaled, scale);
    }
}