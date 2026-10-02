package com.api.alquilaya.repositories;

import com.api.alquilaya.models.AlquilerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlquilerRepository extends JpaRepository<AlquilerModel, Long> {
}