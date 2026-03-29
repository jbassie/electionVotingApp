package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.*;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionTypeRepository;
import dreamdev.moniepoint.data.repositories.RegisteredVotersRepository;
import dreamdev.moniepoint.data.repositories.VoteRepository;
import dreamdev.moniepoint.dtos.request.CastVoteRequest;
import dreamdev.moniepoint.dtos.response.CastVoteResponse;
import dreamdev.moniepoint.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static dreamdev.moniepoint.utils.VoteMapper.map;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private RegisteredVotersRepository registeredVotersRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ElectionTypeRepository electionTypeRepository;

    @Override
    public CastVoteResponse castVote(CastVoteRequest request) {
        RegisteredVoter voter = registeredVotersRepository.findByVoterID(request.getVoterID())
                .orElseThrow(() -> new VoterNotFoundException(
                        "No voter found with ID: " + request.getVoterID()));

        if (!voter.isLoggedIn())
            throw new InvalidVoteException("Voter must be logged in to cast a vote");

        ElectionType election = electionTypeRepository.findById(request.getElectionTypeId())
                .orElseThrow(() -> new ElectionNotFoundException(
                        "No election found with ID: " + request.getElectionTypeId()));

        if (election.getStatus() != ElectionStatus.ACTIVE)
            throw new InvalidVoteException(
                    "Voting is only allowed in ACTIVE elections. Current status: " + election.getStatus());

        if (voteRepository.existsByVoterIdAndElectionTypeId(request.getVoterID(), request.getElectionTypeId()))
            throw new InvalidVoteException(
                    "Voter " + request.getVoterID() + " has already voted in this election");

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new CandidateNotFoundException(
                        "No candidate found with ID: " + request.getCandidateId()));

        if (!candidate.getElectionTypeId().equals(request.getElectionTypeId()))
            throw new InvalidVoteException("Candidate does not belong to this election");

        if (candidate.getRegisteredVoter().getVoterID().equals(request.getVoterID()))
            throw new InvalidVoteException("A voter cannot vote for themselves");

        Vote vote = new Vote();
        vote.setVoterId(request.getVoterID());
        vote.setCandidate(candidate);
        vote.setCandidateId(candidate.getId());
        vote.setElectionTypeId(request.getElectionTypeId());
        vote.setVotedAt(LocalDateTime.now());
        Vote savedVote = voteRepository.save(vote);

        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateRepository.save(candidate);

        voter.getHasVoted().put(request.getElectionTypeId(), true);
        registeredVotersRepository.save(voter);

        return map(savedVote, election);
    }

}
