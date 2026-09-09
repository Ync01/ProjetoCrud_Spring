package br.gov.sp.cps.demo.model;


public class VeiculoDTO {
    private String placa;
    private String modelo;
    private String color;
    private String observacao;

    public VeiculoDTO() {
    }

    public VeiculoDTO(String placa, String modelo, String color, String observacao) {
        this.placa = placa;
        this.modelo = modelo;
        this.color = color;
        this.observacao = observacao;
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
