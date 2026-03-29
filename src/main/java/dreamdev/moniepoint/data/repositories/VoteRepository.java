package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String> {

    boolean existsByVoterIdAndElectionTypeId(String voterId, String electionTypeId);

    List<Vote> findAllByElectionTypeId(String electionTypeId);

    long countByElectionTypeIdAndCandidateId(String electionTypeId, String candidateId);

}
