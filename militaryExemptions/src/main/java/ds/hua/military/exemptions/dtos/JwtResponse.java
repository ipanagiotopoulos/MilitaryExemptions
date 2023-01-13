package ds.hua.military.exemptions.dtos;

import ds.hua.military.exemptions.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Role role;

    public JwtResponse(String token, String username, Role role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

}
