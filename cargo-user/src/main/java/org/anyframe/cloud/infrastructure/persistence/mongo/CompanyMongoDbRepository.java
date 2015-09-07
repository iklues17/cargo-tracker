package org.anyframe.cloud.infrastructure.persistence.mongo;

import org.anyframe.cloud.domain.Company;
import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.infrastructure.persistence.RegisteredUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CompanyMongoDbRepository extends RegisteredUserRepository, MongoRepository<Company, String> {
}
