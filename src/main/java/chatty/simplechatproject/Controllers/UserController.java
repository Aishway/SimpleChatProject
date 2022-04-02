package chatty.simplechatproject.Controllers;

import chatty.simplechatproject.Models.DTO.UserDTO;
import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.UserRepository;
import chatty.simplechatproject.Security.JwtTokenUtil;
import chatty.simplechatproject.Services.UserDetailService;
import chatty.simplechatproject.Services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final JwtTokenUtil jwtUtil;
    private final UserDetailService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserRepository userRepository,
                          UserServiceImpl userService,
                          JwtTokenUtil jwtUtil,
                          UserDetailService userDetailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    //A new user can register into the application here
    @PostMapping("/registration")
    public ResponseEntity registrationNewUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing username or password!");
        } else if (userRepository.findByUserName(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists!");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.registration(userDTO));
        }
    }

    // A user can log in into the app
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
        if (userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing username or password!");
        } else if (userRepository.findByUserName(userDTO.getUsername()).isPresent()) {
            // try - catch block check, if user enter wrong username or password
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username and/or password was incorrect!");
            }

            // if everything is OK, the application will generate a JWT token.
            // I used JWT because through authentication we can easily find out the current user
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jwtUtil.generateToken(userDetailService.loadUserByUsername(userDTO.getUsername())));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found!");
        }
    }

    // with this endpoint user can log out of the application. Logout endpoint only set user online status as false.
    //The user cannot send a message to offline users.
    @GetMapping("/logout")
    public ResponseEntity logout(Authentication authentication) {
        User user = userRepository.findUserByUserName(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(userService.logout(user));
    }

    // with this endpoint user can list all online users (online users are those users who have logged in and have
    // status online = true)
    @GetMapping("/online")
    public ResponseEntity listAllOnlineUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.showAllOnlineUsers());
    }
}
