package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.request.NominateCandidateRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.exceptions.VotingAppException;
import dreamdev.moniepoint.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping("/nominate")
    public ResponseEntity<?> nominate(@RequestBody NominateCandidateRequest request) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, candidateService.nominate(request)), HttpStatus.CREATED);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{electionTypeId}")
    public ResponseEntity<?> getCandidates(@PathVariable("electionTypeId") String electionTypeId) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, candidateService.getCandidatesByElection(electionTypeId)), HttpStatus.OK);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
