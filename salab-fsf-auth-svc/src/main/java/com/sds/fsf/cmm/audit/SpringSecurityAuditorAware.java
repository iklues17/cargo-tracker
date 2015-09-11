package com.sds.fsf.cmm.audit;

import com.sds.fsf.cmm.security.util.SecurityUtils;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentLogin();
        return (userName != null ? userName : "system");
    }
}
