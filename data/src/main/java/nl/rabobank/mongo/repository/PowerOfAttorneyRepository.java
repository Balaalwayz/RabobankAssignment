package nl.rabobank.mongo.repository;

import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerOfAttorneyRepository extends MongoRepository<PowerOfAttorneyEntity, String> {

    /**
     * Query to find the list of Power Of Attorneys executed so far
     * for a particular grantee
     * @param granteeName
     * @return
     */
    List<PowerOfAttorneyEntity> findAllByPowerOfAttorneyGranteeName(String granteeName);
}
