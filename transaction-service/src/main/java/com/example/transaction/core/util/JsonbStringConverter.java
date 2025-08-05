package com.example.transaction.core.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.SQLException;

import org.postgresql.util.PGobject;

import com.example.transaction.core.exception.TransactionServiceException;

@Converter
public class JsonbStringConverter implements AttributeConverter<String, Object> {

    @Override
    public Object convertToDatabaseColumn(String attribute) {
        var pgObject = new PGobject();
        try {
            pgObject.setType("jsonb");
            pgObject.setValue(attribute);
        } catch (SQLException e) {
            throw new TransactionServiceException("Failed to convert String to jsonb PGobject", e);
        }
        return pgObject;
    }

    @Override
    public String convertToEntityAttribute(Object dbData) {
        return dbData.toString();
    }
}