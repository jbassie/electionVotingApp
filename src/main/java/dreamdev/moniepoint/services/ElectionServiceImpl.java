package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.ElectionStatus;
import dreamdev.moniepoint.data.models.ElectionType;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionTypeRepository;
import dreamdev.moniepoint.dtos.request.CreateElectionRequest;
import dreamdev.moniepoint.dtos.response.ElectionResponse;
import dreamdev.moniepoint.dtos.response.ElectionResultsResponse;
import dreamdev.moniepoint.exceptions.ElectionNotFoundException;
import dreamdev.moniepoint.exceptions.InvalidElectionStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dreamdev.moniepoint.utils.ElectionMapper.map;
import static dreamdev.moniepoint.utils.ElectionMapper.mapToResults;

@Service
public class ElectionServiceImpl implements ElectionService {

    @Autowired
    private ElectionTypeRepository electionTypeRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public ElectionResponse createElection(CreateElectionRequest request) {
        ElectionType election = new ElectionType();
        election.setName(request.getName());
        election.setDescription(request.getDescription());
        election.setCreatedAt(LocalDateTime.now());
        return map(electionTypeRepository.save(election));
    }

    @Override
    public ElectionResponse startElection(String electionTypeId) {
        ElectionType election = electionTypeRepository.findById(electionTypeId)
                .orElseThrow(() -> new ElectionNotFoundException(
                        "No election found with ID: " + electionTypeId));

        if (election.getStatus() != ElectionStatus.PENDING)
            throw new InvalidElectionStateException(
                    "Election must be PENDING to start. Current status: " + election.getStatus());

        election.setStatus(ElectionStatus.ACTIVE);
        return map(electionTypeRepository.save(election));
    }

    @Override
    public ElectionResponse closeElection(String electionTypeId) {
        ElectionType election = electionTypeRepository.findById(electionTypeId)
                .orElseThrow(() -> new ElectionNotFoundException(
                        "No election found with ID: " + electionTypeId));

        if (election.getStatus() != ElectionStatus.ACTIVE)
            throw new InvalidElectionStateException(
                    "Election must be ACTIVE to close. Current status: " + election.getStatus());

        election.setStatus(ElectionStatus.CLOSED);
        return map(electionTypeRepository.save(election));
    }

    @Override
    public ElectionResultsResponse getResults(String electionTypeId) {
        ElectionType election = electionTypeRepository.findById(electionTypeId)
                .orElseThrow(() -> new ElectionNotFoundException(
                        "No election found with ID: " + electionTypeId));

        if (election.getStatus() != ElectionStatus.CLOSED)
            throw new InvalidElectionStateException(
                    "Results are only available for CLOSED elections. Current status: " + election.getStatus());

        List<Candidate> sortedCandidates =
                candidateRepository.findAllByElectionTypeIdOrderByVoteCountDesc(electionTypeId);

        return mapToResults(election, sortedCandidates);
    }

}
