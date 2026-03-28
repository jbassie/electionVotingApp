package dreamdev.moniepoint.data.repositories;
import dreamdev.moniepoint.data.models.Citizen;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CitizensRepository extends MongoRepository<Citizen, String> {

    Optional<Citizen> findByNationalID(String nationalID);
}
