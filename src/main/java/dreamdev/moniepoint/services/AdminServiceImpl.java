package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Admin;
import dreamdev.moniepoint.data.repositories.AdminRepository;
import dreamdev.moniepoint.dtos.request.AdminLoginRequest;
import dreamdev.moniepoint.dtos.request.AdminRegistrationRequest;
import dreamdev.moniepoint.dtos.response.AdminLoginResponse;
import dreamdev.moniepoint.dtos.response.AdminRegistrationResponse;
import dreamdev.moniepoint.exceptions.AdminNotFoundException;
import dreamdev.moniepoint.exceptions.AlreadyRegisteredException;
import dreamdev.moniepoint.exceptions.InvalidLoginDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public AdminRegistrationResponse register(AdminRegistrationRequest request) {
        if (adminRepository.findByUsername(request.getUsername()).isPresent())
            throw new AlreadyRegisteredException("Admin with username '" + request.getUsername() + "' already exists");

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        admin.setCreatedAt(LocalDateTime.now());

        Admin saved = adminRepository.save(admin);
        return new AdminRegistrationResponse(saved.getId(), saved.getUsername(), saved.getEmail());
    }

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AdminNotFoundException(
                        "No admin found with username: " + request.getUsername()));

        if (!admin.getPassword().equals(request.getPassword()))
            throw new InvalidLoginDetailsException("Incorrect password");

        admin.setLoggedIn(true);
        adminRepository.save(admin);

        return new AdminLoginResponse(admin.getId(), admin.getUsername(), true);
    }
}
