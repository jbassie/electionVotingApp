package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.request.CastVoteRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.exceptions.VotingAppException;
import dreamdev.moniepoint.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/cast")
    public ResponseEntity<?> castVote(@RequestBody CastVoteRequest request) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, voteService.castVote(request)), HttpStatus.CREATED);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
