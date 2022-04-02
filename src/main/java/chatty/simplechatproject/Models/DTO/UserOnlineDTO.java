package chatty.simplechatproject.Models.DTO;

import lombok.Data;

@Data
public class UserOnlineDTO {

    private String username;
    private String status;

    public UserOnlineDTO(String username) {
        this.username = username;
        this.status = "Online";
    }
}
