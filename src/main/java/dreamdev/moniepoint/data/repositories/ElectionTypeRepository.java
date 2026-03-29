package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.ElectionType;
import dreamdev.moniepoint.data.models.ElectionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ElectionTypeRepository extends MongoRepository<ElectionType, String> {
    Optional<ElectionType> findByName(String name);
    List<ElectionType> findByStatus(ElectionStatus status);
}