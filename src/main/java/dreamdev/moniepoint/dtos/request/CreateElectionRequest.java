package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class CreateElectionRequest {
    private String adminId;
    private String name;
    private String description;
    private String type;
    private String startDate;
    private String endDate;
}
