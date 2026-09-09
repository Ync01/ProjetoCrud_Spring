package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Veiculo;
import br.gov.sp.cps.demo.model.VeiculoDTO;
import br.gov.sp.cps.demo.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Override
    public String cadrastoVeiculo(VeiculoDTO veiculoDTO) {
        Veiculo veiculo = new Veiculo();

        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setColor(veiculoDTO.getColor());
        veiculo.setModelo(veiculoDTO.getModelo());
        veiculo.setObservacao(veiculoDTO.getObservacao());

        veiculoRepository.save(veiculo);

        return "Veiculo criado com sucesso!";
    }

    @Override
    public List<VeiculoDTO> listarVeiculos() {
        return List.of();
    }

    @Override
    public VeiculoDTO buscarPorPlaca(String placa) {


        Veiculo veiculo = veiculoRepository.findByPlaca(placa);

        if (veiculo == null){
            return null;
        }
        return new VeiculoDTO(
                veiculo.getPlaca(),
                veiculo.getColor(),
                veiculo.getModelo(),
                veiculo.getObservacao()
        );
    }

    @Override
    public String atualizar(Long id, VeiculoDTO veiculo) {
        return "";
    }

    @Override
    public String deletar(Long id) {
        return "";
    }
}
