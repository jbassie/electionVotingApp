package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.exceptions.VotingAppException;
import dreamdev.moniepoint.services.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitizenController {
    @Autowired

    private CitizenService citizenService;

    @PostMapping("/citizens/register")
    public ResponseEntity<?> register(@RequestBody CitizenRegistrationRequest citizenRegistrationRequest ){
        try{
            return new ResponseEntity<>(new ApiResponse(true, citizenService.register(citizenRegistrationRequest)), HttpStatus.CREATED);
        }catch (VotingAppException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
