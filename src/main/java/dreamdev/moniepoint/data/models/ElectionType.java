package dreamdev.moniepoint.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class ElectionType {

    @Id
    private String id;
    private String name;
    private String description;
    private String type;
    private ElectionStatus status = ElectionStatus.PENDING;
    private List<String> candidateIds = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
}