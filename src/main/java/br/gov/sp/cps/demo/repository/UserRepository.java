package br.gov.sp.cps.demo.repository;

import br.gov.sp.cps.demo.entities.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuarios, Long> {

}
