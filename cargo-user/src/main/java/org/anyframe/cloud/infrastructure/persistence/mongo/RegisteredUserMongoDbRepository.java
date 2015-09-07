package org.anyframe.cloud.infrastructure.persistence.mongo;

import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.infrastructure.persistence.RegisteredUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RegisteredUserMongoDbRepository extends RegisteredUserRepository, MongoRepository<RegisteredUser, String> {
}
