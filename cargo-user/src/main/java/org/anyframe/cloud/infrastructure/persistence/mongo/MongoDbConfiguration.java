package org.anyframe.cloud.infrastructure.persistence.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages="org.anyframe.cloud.infrastructure.persistence.mongo")
@EnableMongoAuditing
public class MongoDbConfiguration extends AbstractMongoConfiguration {

//    @Value("${spring.data.mongodb.host}")
	private String mongoHost = "70.121.244.13";

//    @Value("${spring.data.mongodb.port}")
	private String mongoPort = "27017";

//    @Value("${spring.data.mongodb.database}")
	private String mongoDB = "user";

	@Override
	protected String getDatabaseName() {
		return mongoDB;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(mongoHost + ":" + mongoPort);
	}

	@Bean
	public AuditorAware<String> myAuditorProvider() {
		return new AuditorAwareImpl();
	}

}
