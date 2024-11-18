package com.botTelRadar.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.botTelRadar.model.VeiculoInfo;
import com.botTelRadar.repository.VeiculoInfoRepository;

@Service
public class VeiculoInfoServiceImpl implements VeiculoInfoService {

    private final VeiculoInfoRepository veiculoInfoRepository;

    @Autowired
    public VeiculoInfoServiceImpl(VeiculoInfoRepository veiculoInfoRepository) {
        this.veiculoInfoRepository = veiculoInfoRepository;
    }

    @Override
    public VeiculoInfo saveVeiculoInfo(VeiculoInfo veiculoInfo) {
        return veiculoInfoRepository.save(veiculoInfo);
    }

    @Override
    public Optional<VeiculoInfo> findByNumeroPlaca(String numeroPlaca) {
        return veiculoInfoRepository.findByNumeroPlaca(numeroPlaca);
    }

    @Override
    public Optional<VeiculoInfo> findById(Long id) {
        return veiculoInfoRepository.findById(id);
    }

    @Override
    public void deleteVeiculoInfo(Long id) {
        veiculoInfoRepository.deleteById(id);
    }
}
