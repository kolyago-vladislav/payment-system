package com.example.currency.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.currency.model.entity.RateProvider;

public interface RateProviderRepository extends JpaRepository<RateProvider, String> {

}
