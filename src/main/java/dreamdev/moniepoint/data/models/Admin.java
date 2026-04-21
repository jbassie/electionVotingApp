package dreamdev.moniepoint.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Admin {
    @Id
    private String id;

    private String username;
    private String password;
    private boolean isLoggedIn;
    private LocalDateTime createdAt;
}
