package com.example.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
