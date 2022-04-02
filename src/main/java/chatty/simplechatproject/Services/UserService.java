package chatty.simplechatproject.Services;

import chatty.simplechatproject.Models.DTO.UserDTO;
import chatty.simplechatproject.Models.DTO.UserOnlineDTO;
import chatty.simplechatproject.Models.User;

import java.util.List;

public interface UserService {

    String registration(UserDTO userDTO);

    String logout(User user);

    List<UserOnlineDTO> showAllOnlineUsers();
}
