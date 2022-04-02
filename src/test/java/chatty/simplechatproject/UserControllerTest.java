package chatty.simplechatproject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.UserRepository;
import chatty.simplechatproject.Security.JwtTokenUtil;
import chatty.simplechatproject.Services.UserDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private User user;
    private final UserRepository userRepository;
    private String token;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    public UserControllerTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private MockMvc mockMvc;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void registration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/registration")
                        .content("{\"username\":\"TestUser\",\"password\": \"1234\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created. You can log in now."));
    }

    @Test
    public void registrationWithSameUsername() throws Exception {
        this.user = new User();
        user.setUserName("Test");
        user.setPassword("test");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/registration")
                        .content("{\"username\":\"Test\",\"password\": \"1234\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists!"));
    }

    @Test
    public void registrationWithEmptyBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/registration")
                        .content("{\"username\":\"\",\"password\": \"1234\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Missing username or password!"));
    }

    @Test
    public void loginWithMissingUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("{\"username\":\"\",\"password\": \"1234\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Missing username or password!"));
    }

    @Test
    public void loginWithWrongPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("{\"username\":\"TestUser\",\"password\": \"123\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Username and/or password was incorrect!"));
    }

    @Test
    public void loginWithRightPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("{\"username\":\"TestUser\",\"password\": \"1234\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void logout() throws Exception {
        token = jwtTokenUtil.generateToken(userDetailService.loadUserByUsername("TestUser"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/logout")
                        .header("Authorization", "Bearer " + token.substring(20).trim()))
                .andExpect(status().isOk())
                .andExpect(content().string("You have been logged out."));
    }

    @Test
    public void showOnlineUsers() throws Exception {
        token = jwtTokenUtil.generateToken(userDetailService.loadUserByUsername("TestUser"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/online")
                        .header("Authorization", "Bearer " + token.substring(20).trim()))
                .andExpect(status().isOk());
    }
}
