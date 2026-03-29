package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    List<Candidate> findByElectionTypeId(String electionTypeId);
}