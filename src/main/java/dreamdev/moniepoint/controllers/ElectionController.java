package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.request.CreateElectionRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.exceptions.VotingAppException;
import dreamdev.moniepoint.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/election")
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @GetMapping
    public ResponseEntity<?> getAllElections() {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, electionService.getAllElections()), HttpStatus.OK);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createElection(@RequestBody CreateElectionRequest request) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, electionService.createElection(request)), HttpStatus.CREATED);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{electionTypeId}/start")
    public ResponseEntity<?> startElection(@PathVariable String electionTypeId) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, electionService.startElection(electionTypeId)), HttpStatus.OK);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{electionTypeId}/close")
    public ResponseEntity<?> closeElection(@PathVariable String electionTypeId) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, electionService.closeElection(electionTypeId)), HttpStatus.OK);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{electionTypeId}/results")
    public ResponseEntity<?> getResults(@PathVariable String electionTypeId) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, electionService.getResults(electionTypeId)), HttpStatus.OK);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
