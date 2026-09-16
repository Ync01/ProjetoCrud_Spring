package br.gov.sp.cps.demo.model;

import java.time.LocalDateTime;

public class VeiculoDTO {
    private Long id;
    private String placa;
    private String modelo;
    private String color;
    private String observacao;
    private LocalDateTime dataEntrada;

    public VeiculoDTO() {
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public VeiculoDTO(Long id, String placa, String modelo, String color, String observacao, LocalDateTime dataEntrada) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.color = color;
        this.observacao = observacao;
        this.dataEntrada = dataEntrada;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}