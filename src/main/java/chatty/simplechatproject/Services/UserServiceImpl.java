package chatty.simplechatproject.Services;

import chatty.simplechatproject.Models.DTO.UserDTO;
import chatty.simplechatproject.Models.DTO.UserOnlineDTO;
import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String registration(UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setOnline(false);
        userRepository.save(user);
        return "User created. You can log in now.";
    }

    @Override
    public String logout(User user) {
        user.setOnline(false);
        userRepository.save(user);
        return "You have been logged out.";
    }

    @Override
    public List<UserOnlineDTO> showAllOnlineUsers() {
        List<UserOnlineDTO> onlineDTOs = new ArrayList<>();
        List<User> onlineUsers = userRepository.findOnlineUsers();
        onlineUsers.forEach(e -> onlineDTOs.add(new UserOnlineDTO(e.getUserName())));
        return onlineDTOs;
    }
}
