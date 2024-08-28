package br.com.garage.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.garage.auth.models.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

	@Query("SELECT e FROM Tenant e WHERE e.cnpj = :cnpj")
    Optional<Tenant> buscarPorCnpj(String cnpj);

    @Query(value = "SELECT * FROM tb_tenants WHERE endereco::json->>'cidade' = :cidade", nativeQuery = true)
    List<Tenant> buscarPorCidade(String cidade);

}
