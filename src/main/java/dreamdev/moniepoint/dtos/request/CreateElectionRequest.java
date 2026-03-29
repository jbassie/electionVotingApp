package dreamdev.moniepoint.dtos.request;

import lombok.Data;

@Data
public class CreateElectionRequest {
    private String name;
    private String description;
}
