package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Veiculo;
import br.gov.sp.cps.demo.model.VeiculoDTO;
import br.gov.sp.cps.demo.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        veiculo.setDataEntrada(LocalDateTime.now());

        veiculoRepository.save(veiculo);

        return "Veículo criado com sucesso!";
    }

    @Override
    public List<VeiculoDTO> listarVeiculos() {
        List<VeiculoDTO> veiculos = new ArrayList<>();

        for (Veiculo veiculo : veiculoRepository.findAll()) {
            veiculos.add(new VeiculoDTO(
                    veiculo.getPrimaryKey(),
                    veiculo.getPlaca(),
                    veiculo.getModelo(),
                    veiculo.getColor(),
                    veiculo.getObservacao(),
                    veiculo.getDataEntrada()
            ));
        }

        return veiculos;
    }

    @Override
    public VeiculoDTO buscarPorId(Long id) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(id);


        Veiculo v = veiculo.get();

        return new VeiculoDTO(
                v.getPrimaryKey(),
                v.getPlaca(),
                v.getModelo(),
                v.getColor(),
                v.getObservacao(),
                v.getDataEntrada()
        );
    }

    @Override
    public VeiculoDTO buscarPorPlaca(String placa) {
        Veiculo veiculo = veiculoRepository.findByPlaca(placa);

        return new VeiculoDTO(
                veiculo.getPrimaryKey(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getColor(),
                veiculo.getObservacao(),
                veiculo.getDataEntrada()
        );
    }

    @Override
    public String atualizar(Long id, VeiculoDTO veiculoDTO) {
        Optional<Veiculo> veiculoExistente = veiculoRepository.findById(id);

        Veiculo veiculo = veiculoExistente.get();
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setColor(veiculoDTO.getColor());
        veiculo.setModelo(veiculoDTO.getModelo());
        veiculo.setObservacao(veiculoDTO.getObservacao());

        veiculoRepository.save(veiculo);

        return "Veículo atualizado com sucesso!";
    }

    @Override
    public String deletar(Long id) {

        veiculoRepository.deleteById(id);
        return "Deletado com sucesso";

    }
}