package com.example.transaction.core.util;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.core.exception.TransactionServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JsonWrapper {

    private final ObjectMapper objectMapper;

    public <T> T read(byte[] s, Class<T> type) {
        try {
            return objectMapper.readValue(s, type);
        } catch (Exception e) {
            throw new TransactionServiceException("Can not parse json string: %s", s);
        }
    }

    public <T> T read(String s, Class<T> type) {
        try {
            return objectMapper.readValue(s, type);
        } catch (Exception e) {
            throw new TransactionServiceException("Can not parse json string: %s", s);
        }
    }

    public <T> String write(T o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new TransactionServiceException("Can not write json string: %s", o);
        }
    }
}
