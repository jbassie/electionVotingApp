package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.data.repositories.CitizensRepository;
import dreamdev.moniepoint.dtos.request.CitizenRegistrationRequest;
import dreamdev.moniepoint.dtos.response.ApiResponse;
import dreamdev.moniepoint.dtos.response.CitizenRegistrationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class CitizensControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private CitizensRepository citizensRepository;

    @BeforeEach
    void setUp() {
        citizensRepository.deleteAll();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test successful user registration")
    void testRegisterSuccess() {
        CitizenRegistrationRequest request = new CitizenRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setGender("Male");
        request.setDateOfBirth("1992-02-19");
        request.setStateOfOrigin("Cross_River");


        CitizenRegistrationResponse citizenRegistrationResponse = new CitizenRegistrationResponse();
        citizenRegistrationResponse.setFirstName("John");
        citizenRegistrationResponse.setLastName("Doe");
        citizenRegistrationResponse.setNationalID("NGN0123456789");



        ApiResponse response = new ApiResponse(true, citizenRegistrationResponse);


        restTestClient.post()
                .uri("http://localhost:%d/citizen".formatted(port))
                .body(request)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .json(objectMapper.writeValueAsString(response));
    }

}
