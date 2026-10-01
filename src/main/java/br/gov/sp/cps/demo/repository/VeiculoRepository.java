package br.gov.sp.cps.demo.repository;

import br.gov.sp.cps.demo.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    // Veículo que ainda está no pátio (sem data de saída)
    Optional<Veiculo> findByPlacaAndDataSaidaIsNull(String placa);

    boolean existsByPlacaAndDataSaidaIsNull(String placa);

    // Registro mais recente da placa (pode já ter saído)
    Optional<Veiculo> findFirstByPlacaOrderByDataEntradaDesc(String placa);

    List<Veiculo> findByDataSaidaIsNullOrderByDataEntradaDesc();

    List<Veiculo> findByDataSaidaIsNotNullOrderByDataSaidaDesc();

}
