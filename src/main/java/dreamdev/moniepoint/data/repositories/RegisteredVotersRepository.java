package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RegisteredVotersRepository extends MongoRepository<RegisteredVoter, String> {

    Optional<RegisteredVoter> findByCitizenNationalID(String nationalID);
    Optional<RegisteredVoter> findByVoterID(String votersID);
}
