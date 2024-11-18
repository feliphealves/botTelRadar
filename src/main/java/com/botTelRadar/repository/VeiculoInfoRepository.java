package com.botTelRadar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botTelRadar.model.VeiculoInfo;

public interface VeiculoInfoRepository extends JpaRepository<VeiculoInfo, Long> {
    Optional<VeiculoInfo> findByNumeroPlaca(String numeroPlaca);
}