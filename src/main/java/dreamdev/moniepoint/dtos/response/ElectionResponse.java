package dreamdev.moniepoint.dtos.response;

import dreamdev.moniepoint.data.models.ElectionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ElectionResponse {

    private String electionTypeId;
    private String name;
    private String description;
    private String type;
    private ElectionStatus status;
    private List<String> candidateIds;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

}
