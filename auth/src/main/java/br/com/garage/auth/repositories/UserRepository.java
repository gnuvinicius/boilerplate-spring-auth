package br.com.garage.auth.repositories;

import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.commons.enums.EnumStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.status = :status")
	Optional<Usuario> buscaPorEmail(String email, EnumStatus status);
	
	@Query("SELECT u FROM Usuario u WHERE u.status = :status AND u.tenant = :tenant")
	List<Usuario> buscaPorTenant(EnumStatus status, Tenant tenant);

}