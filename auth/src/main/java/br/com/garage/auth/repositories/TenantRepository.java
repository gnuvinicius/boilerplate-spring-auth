package br.com.garage.auth.repositories;

import br.com.garage.auth.models.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

	@Query("SELECT e FROM Tenant e WHERE e.cnpj = :cnpj")
    Optional<Tenant> buscarPorCnpj(String cnpj);

}
