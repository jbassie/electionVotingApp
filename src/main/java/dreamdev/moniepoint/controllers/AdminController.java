package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.request.AdminLoginRequest;
import dreamdev.moniepoint.dtos.request.AdminRegistrationRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.exceptions.VotingAppException;
import dreamdev.moniepoint.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminRegistrationRequest request) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, adminService.register(request)), HttpStatus.CREATED);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        try {
            return new ResponseEntity<>(
                    new ApiResponse(true, adminService.login(request)), HttpStatus.OK);
        } catch (VotingAppException e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
