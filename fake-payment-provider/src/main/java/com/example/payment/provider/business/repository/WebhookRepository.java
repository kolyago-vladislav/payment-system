package com.example.payment.provider.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payment.provider.model.entity.Webhook;

public interface WebhookRepository extends JpaRepository<Webhook, Long> {

}
