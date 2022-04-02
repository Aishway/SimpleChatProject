package chatty.simplechatproject;

import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.MessageRepository;
import chatty.simplechatproject.Repositories.UserRepository;
import chatty.simplechatproject.Security.JwtTokenUtil;
import chatty.simplechatproject.Services.UserDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private String token;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    public MessageControllerTest(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void sendMessageTest() throws Exception {
        User userOne = new User(1l, "Alan", passwordEncoder.encode("123"),
                true, new ArrayList<>());
        User secondOne = new User(2l, "Delon", passwordEncoder.encode("123"),
                true, new ArrayList<>());

        userRepository.save(userOne);
        userRepository.save(secondOne);

        this.token = jwtTokenUtil.generateToken(userDetailService.loadUserByUsername(userOne.getUserName()));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/send/2")
                        .content("\"Here is your message.\"}")
                        .header("Authorization", "Bearer " + token.substring(20).trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        int i = messageRepository.findAllMessages(userOne.getId(),
                secondOne.getUserName(),
                secondOne.getId(),
                userOne.getUserName()).size();

        assertEquals(1, i);
    }
}
