package com.example.transaction.util;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomUUIDDeserializer extends JsonDeserializer<UUID> {
    @Override
    public UUID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String uuidStr = p.getText();
        // Например, если uuidStr содержит лишние символы, очисти:
        uuidStr = uuidStr.trim();
        return UUID.fromString(uuidStr);
    }
}