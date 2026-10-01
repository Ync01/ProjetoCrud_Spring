package br.gov.sp.cps.demo.model;

import java.time.LocalDateTime;

public class VeiculoDTO {

    private Long id;
    private String placa;
    private String modelo;
    private String cor;
    private String observacao;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;

    public VeiculoDTO() {
    }

    public VeiculoDTO(Long id, String placa, String modelo, String cor, String observacao,
                      LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.observacao = observacao;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }
}
