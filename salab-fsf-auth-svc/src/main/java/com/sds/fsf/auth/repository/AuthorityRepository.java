package com.sds.fsf.auth.repository;

import java.util.List;

import com.sds.fsf.auth.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
	
	List<Authority> findByNameStartingWith(String name);
}
