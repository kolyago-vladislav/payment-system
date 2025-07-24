package com.example.transaction.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BigDecimalFromObjectDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var codec = p.getCodec();
        var node = (JsonNode) codec.readTree(p);

        var scale = node.get("scale").asInt();
        var base64 = node.get("value").asText();
        var decoded = Base64.getDecoder().decode(base64);
        var unscaled = new BigInteger(decoded);

        return new BigDecimal(unscaled, scale);
    }
}