package com.sds.fsf.auth.repository;

import com.sds.fsf.auth.domain.User;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime);

    User findOneByLogin(String login);
    
    User findOneByLoginAndActivatedIsTrue(String login);

    User findOneByEmail(String email);
    
    @Query(value = "select u from User u "
    		+ " join fetch "
    		+ " u.authorities ua "
    		+ " where (u.activated = true or :activated = false) "
    		+ " and ua.name in :authorityNames "
    		+ " and u.login like concat('%',:login,'%')"
    		+ " and lower(coalesce(u.email,'')) like lower(concat('%',:email,'%'))"
    		+ " and lower(concat(u.firstName,' ',u.lastName)) like lower(concat('%',:userName,'%'))"
    		+ " and coalesce(u.mobilePhoneNumber,'') like concat('%',:mobilePhoneNumber,'%')"
    		,
    		countQuery = "select count(u) from User u "
    				+ " join "
    				+ " u.authorities ua "
    	    		+ " where (u.activated = true or :activated = false) "
    	    		+ " and ua.name in :authorityNames "
    	    		+ " and u.login like concat('%',:login,'%')"
    	    		+ " and lower(coalesce(u.email,'')) like lower(concat('%',:email,'%'))"
    	    		+ " and lower(concat(u.firstName,' ',u.lastName)) like lower(concat('%',:userName,'%'))"
    	    		+ " and coalesce(u.mobilePhoneNumber,'') like concat('%',:mobilePhoneNumber,'%')"
    	    		)
    Page<User> findAllByActivatedAndAuthorityNameIn(@Param("activated") Boolean activated, 
    		@Param("authorityNames") Collection<String> authorityNames,
    		@Param("login") String login,
    		@Param("email") String email,
    		@Param("userName") String userName,
    		@Param("mobilePhoneNumber") String mobilePhoneNumber,
    		Pageable pageable);

}
