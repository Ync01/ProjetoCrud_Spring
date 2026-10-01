package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.model.VeiculoDTO;

import java.util.List;

public interface VeiculoService {

    String cadastrarVeiculo(VeiculoDTO veiculo);

    List<VeiculoDTO> listarVeiculos();

    // Veículos que ainda estão no pátio
    List<VeiculoDTO> listarAtivos();

    // Veículos que já saíram
    List<VeiculoDTO> listarHistorico();

    VeiculoDTO buscarPorId(Long id);

    VeiculoDTO buscarPorPlaca(String placa);

    String atualizar(Long id, VeiculoDTO veiculo);

    String deletar(Long id);

    String darSaida(Long id);

}
