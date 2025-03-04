package com.ptlink.ptlink_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptlink.ptlink_server.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
}
