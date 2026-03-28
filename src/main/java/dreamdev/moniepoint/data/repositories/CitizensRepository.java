package dreamdev.moniepoint.data.repositories;
import dreamdev.moniepoint.data.models.Citizens;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CitizensRepository extends MongoRepository<Citizens, String> {

    Optional<Citizens> findByNationalId(String nationalID);
}
