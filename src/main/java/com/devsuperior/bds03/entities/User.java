package com.devsuperior.bds03.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "tb_user") // será o nome da tabela no banco de dados
public class User implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true) // não pode repetir o email
	private String email;
	private String password;

	@ManyToMany(fetch = FetchType.EAGER) // sempre que buscar um usuário no banco, ja vem os perfis deste usuário
	@JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"), // chave estrangeira referente a
																					// tabela onde estamos, igual no
																					// script sql
			inverseJoinColumns = @JoinColumn(name = "role_id")) // chave estrangeira referente a outra tabela
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(Long id, String email, String password) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// UserDetails:
	// -----------------------------------------------------------------------------
	@Override // Retorna uma coleção do tipo GrantedAuthority
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// vai percorrer a coleção de Roles convetendo para o tipo GrantedAuthority

		// percorrer a coleção convertendo cada tipo role para elemento do tipo
		// GrantedAuthoriry
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
		// SimpleGrantedAuthority = classe concreta que implementa o GrantedAuthority
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
		return true;
	}
}