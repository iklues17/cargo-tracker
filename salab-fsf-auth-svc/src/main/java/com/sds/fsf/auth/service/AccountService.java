package com.sds.fsf.auth.service;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.repository.UserRepository;

@Service
@Transactional
public class AccountService {

	private static final int MINIMUN_LENGTH_OF_RANDOMIZED_PASSWORD = 10;
	
	private final Logger log = LoggerFactory.getLogger(AccountService.class);

	@Inject
	UserRepository userRepository;
	
    @Inject
    private PasswordEncoder passwordEncoder;

	public String getRandomizePassword(String login) {
		User user = userRepository.findOneByLogin(login);
		String password = RandomStringUtils.randomAlphanumeric(MINIMUN_LENGTH_OF_RANDOMIZED_PASSWORD + RandomUtils.nextInt(5));
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setLoginFailureCount(0);
        user.setPasswordUpdatedDate(DateTime.now());
        user.setPasswordExpiredDate(DateTime.now());
        userRepository.save(user);
        log.debug("Randomised password for User: {}", user);
        return password;
	}

}
