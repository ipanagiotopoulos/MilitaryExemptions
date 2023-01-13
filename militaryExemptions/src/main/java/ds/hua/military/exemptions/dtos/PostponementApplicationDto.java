package ds.hua.military.exemptions.dtos;

import ds.hua.military.exemptions.models.enums.Reason;
import ds.hua.military.exemptions.models.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.UUID;
@Data
public class PostponementApplicationDto {

    @NotNull
    private Reason reason;
    @NotNull
    private Long milNumber;

    @Data
    public static class Add extends PostponementApplicationDto {

        @NotNull
        private String citizenUsername;

    }
    @Data
    public static class Update extends PostponementApplicationDto {


    }
    @Data
    public static class Response extends PostponementApplicationDto {
        private UUID id;
        private Status status;
        private Date createdAt;

    }
    @Data
    public static class ChangeStatus  {
        private Status status;

    }
}