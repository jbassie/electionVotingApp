package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Candidate;
import dreamdev.moniepoint.data.models.Citizen;
import dreamdev.moniepoint.data.models.ElectionStatus;
import dreamdev.moniepoint.data.models.ElectionType;
import dreamdev.moniepoint.data.models.RegisteredVoter;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionTypeRepository;
import dreamdev.moniepoint.data.repositories.RegisteredVotersRepository;
import dreamdev.moniepoint.dtos.request.NominateCandidateRequest;
import dreamdev.moniepoint.dtos.response.NominateCandidateResponse;
import dreamdev.moniepoint.exceptions.AlreadyANominatedCandidateException;
import dreamdev.moniepoint.exceptions.ElectionNotFoundException;
import dreamdev.moniepoint.exceptions.InvalidElectionStateException;
import dreamdev.moniepoint.exceptions.SelfNominationException;
import dreamdev.moniepoint.exceptions.VoterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static dreamdev.moniepoint.utils.CandidateMapper.map;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RegisteredVotersRepository registeredVotersRepository;

    @Autowired
    private ElectionTypeRepository electionTypeRepository;

    @Override
    public NominateCandidateResponse nominate(NominateCandidateRequest request) {
        registeredVotersRepository.findByVoterID(request.getNominatorVoterID())
                .orElseThrow(() -> new VoterNotFoundException(
                        "No voter found with nominator ID: " + request.getNominatorVoterID()));

        if (request.getNominatorVoterID().equals(request.getNomineeVoterID()))
            throw new SelfNominationException("A voter cannot nominate themselves");

        RegisteredVoter nominee = registeredVotersRepository.findByVoterID(request.getNomineeVoterID())
                .orElseThrow(() -> new VoterNotFoundException(
                        "No voter found with nominee ID: " + request.getNomineeVoterID()));

        ElectionType election = electionTypeRepository.findById(request.getElectionId())
                .orElseThrow(() -> new ElectionNotFoundException(
                        "No election found with ID: " + request.getElectionId()));

        if (election.getStatus() != ElectionStatus.PENDING)
            throw new InvalidElectionStateException(
                    "Candidates can only be nominated in PENDING elections. Current status: " + election.getStatus());

        if (candidateRepository.existsByRegisteredVoterAndElectionTypeId(nominee, request.getElectionId()))
            throw new AlreadyANominatedCandidateException(
                    "Voter " + request.getNomineeVoterID() + " is already a candidate in this election");

        Candidate candidate = new Candidate();
        candidate.setRegisteredVoter(nominee);
        candidate.setElectionTypeId(request.getElectionId());
        candidate.setNominatedAt(LocalDateTime.now());
        Candidate saved = candidateRepository.save(candidate);

        if (election.getCandidateIds() == null) election.setCandidateIds(new java.util.ArrayList<>());
        election.getCandidateIds().add(saved.getId());
        electionTypeRepository.save(election);

        List<String> otherCandidateNames = getOtherCandidateNames(saved.getId(), request.getElectionId());
        return map(saved, election, otherCandidateNames);
    }

    @Override
    public List<NominateCandidateResponse> getCandidatesByElection(String electionTypeId) {
        ElectionType election = electionTypeRepository.findById(electionTypeId)
                .orElseThrow(() -> new ElectionNotFoundException(
                        "No election found with ID: " + electionTypeId));

        return candidateRepository.findByElectionTypeId(electionTypeId)
                .stream()
                .map(candidate -> map(candidate, election, getOtherCandidateNames(candidate.getId(), electionTypeId)))
                .collect(Collectors.toList());
    }

    private List<String> getOtherCandidateNames(String excludeCandidateId, String electionTypeId) {
        return candidateRepository.findByElectionTypeId(electionTypeId)
                .stream()
                .filter(c -> !c.getId().equals(excludeCandidateId))
                .map(c -> {
                    RegisteredVoter v = c.getRegisteredVoter();
                    Citizen citizen = v.getCitizen();
                    return citizen.getFirstName() + " " + citizen.getLastName();
                })
                .collect(Collectors.toList());
    }

}
