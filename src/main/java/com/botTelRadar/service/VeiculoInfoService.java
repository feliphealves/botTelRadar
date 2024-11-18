package com.botTelRadar.service;

import java.util.Optional;
import com.botTelRadar.model.VeiculoInfo;

public interface VeiculoInfoService {
    VeiculoInfo saveVeiculoInfo(VeiculoInfo veiculoInfo);
    Optional<VeiculoInfo> findByNumeroPlaca(String numeroPlaca);
    Optional<VeiculoInfo> findById(Long id);
    void deleteVeiculoInfo(Long id);
}
