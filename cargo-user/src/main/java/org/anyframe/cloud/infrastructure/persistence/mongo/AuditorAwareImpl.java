package org.anyframe.cloud.infrastructure.persistence.mongo;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String>{

	@Override
	public String getCurrentAuditor() {
		return "system";
	}

}
