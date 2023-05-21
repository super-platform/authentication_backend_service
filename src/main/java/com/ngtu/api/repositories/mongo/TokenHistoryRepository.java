package com.ngtu.api.repositories.mongo;

import com.ngtu.api.entities.TokenHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenHistoryRepository extends MongoRepository<TokenHistory, String> {
}
