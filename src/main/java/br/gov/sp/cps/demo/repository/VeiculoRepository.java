package br.gov.sp.cps.demo.repository;

import br.gov.sp.cps.demo.entities.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Veiculo findByPlaca(String placa);

}
