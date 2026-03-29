package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    List<Candidate> findByElectionTypeId(String electionTypeId);
    List<Candidate> findAllByElectionTypeIdOrderByVoteCountDesc(String electionTypeId);
    boolean existsByRegisteredVoterAndElectionTypeId(RegisteredVoter registeredVoter, String electionTypeId);
}