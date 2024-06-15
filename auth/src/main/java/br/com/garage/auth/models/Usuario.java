package br.com.garage.auth.models;

import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.models.AggregateRoot;
import br.com.garage.commons.utils.AssertionConcern;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_usuarios")
public class Usuario extends AggregateRoot implements UserDetails {

    @Id
    @SequenceGenerator(name = "tb_usuarios_id_seq", sequenceName = "tb_usuarios_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_usuarios_id_seq")
    protected Long id;

    private static final String NULO_OU_VAZIO = "o campo %s não pode ser nulo ou vazio";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    public Tenant tenant;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_usuario_role",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String tokenRefreshPassword;

    private boolean tokenRefreshPasswordValid;

    private Boolean primeiroAcesso;

    private LocalDateTime ultimoAcesso;

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(String email, String nome, boolean primeiroAcesso) {
        super();
        this.email = email;
        this.nome = nome;
        this.primeiroAcesso = primeiroAcesso;
        this.roles = new HashSet<>();
        this.tokenRefreshPasswordValid = false;
    }

    public Usuario(UsuarioRequestDto dto, String passwordEncoded, Tenant tenant) {
        this(dto.getEmail(), dto.getNome(), dto.isPrimeiroAcesso());
        this.password = passwordEncoded;
        this.tenant = tenant;
        valida();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus().equals(EnumStatus.ATIVO);
    }

    public void alteraPassword(String passwordEncoded) {
        this.password = passwordEncoded;
        this.primeiroAcesso = false;
        this.tokenRefreshPasswordValid = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void ativa() {
        this.status = EnumStatus.ATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizaRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void inativa() {
        this.status = EnumStatus.INATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizaDataUltimoLogin() {
        this.ultimoAcesso = LocalDateTime.now();
    }

    public void ativaRefreshToken(String token) {
        this.tokenRefreshPassword = token.replace("/", "");
        this.tokenRefreshPasswordValid = true;
    }

    private void valida() {
        AssertionConcern.ValideIsNotEmptyOrBlank(email, String.format(NULO_OU_VAZIO, "email"));
        AssertionConcern.ValideIsNotEmptyOrBlank(nome, String.format(NULO_OU_VAZIO, "nome"));
        AssertionConcern.ValideIsNotEmptyOrBlank(password, String.format(NULO_OU_VAZIO, "password"));
    }

}