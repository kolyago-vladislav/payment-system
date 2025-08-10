package com.example.currency.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.currency.model.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

}
