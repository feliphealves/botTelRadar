package com.botTelRadar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "veiculo_info")
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String numeroPlaca;
    private String nomeProprietario;
    private String cpfCnpj;
    private String dataPedido;
    private String estado;
    private String cidade;
    private String nomeContato;
    private String contato;
    
    public VeiculoInfo(String numeroPlaca, String nomeProprietario, String cpfCnpj, String dataPedido) {
        this.numeroPlaca = numeroPlaca;
        this.nomeProprietario = nomeProprietario;
        this.cpfCnpj = cpfCnpj;
        this.dataPedido = dataPedido;
    }
}