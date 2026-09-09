package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.model.VeiculoDTO;

import java.util.List;

public interface VeiculoService {

    String cadrastoVeiculo(VeiculoDTO veiculo);

    List<VeiculoDTO> listarVeiculos();

    VeiculoDTO buscarPorPlaca(String placa);

    String atualizar(Long id, VeiculoDTO veiculo);

    String deletar(Long id);

}
