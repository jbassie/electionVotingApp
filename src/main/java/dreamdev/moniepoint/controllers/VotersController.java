package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.request.VotersLoginRequest;
import dreamdev.moniepoint.dtos.request.VotersRegistrationRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.exceptions.VotingAppException;
import dreamdev.moniepoint.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voters")
public class VotersController {


        @Autowired
        private VoterService voterService;

        @PostMapping("register")
        public ResponseEntity<?> register(@RequestBody VotersRegistrationRequest request) {
            try {
                return new ResponseEntity<>(
                        new ApiResponse(true, voterService.register(request)), HttpStatus.CREATED);
            } catch (VotingAppException e) {
                return new ResponseEntity<>(
                        new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody VotersLoginRequest request) {
            try {
                return new ResponseEntity<>(
                        new ApiResponse(true, voterService.login(request)), HttpStatus.OK);
            } catch (VotingAppException e) {
                return new ResponseEntity<>(
                        new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        @PostMapping("/logout/{voterID}")
        public ResponseEntity<?> logout(@PathVariable String voterID) {
            try {
                voterService.logout(voterID);
                return new ResponseEntity<>(
                        new ApiResponse(true, "Voter logged out successfully"), HttpStatus.OK);
            } catch (VotingAppException e) {
                return new ResponseEntity<>(
                        new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }
}
