package chatty.simplechatproject.Controllers;

import chatty.simplechatproject.Models.DTO.MessageBodyDTO;
import chatty.simplechatproject.Models.Message;
import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.MessageRepository;
import chatty.simplechatproject.Repositories.UserRepository;
import chatty.simplechatproject.Services.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageServiceImpl messageService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageController(MessageServiceImpl messageService, MessageRepository messageRepository, UserRepository userRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // here the user can see all previous messages sent to the selected user. The selected user is identified by his ID.
    @GetMapping("show/{id}")
    public ResponseEntity showAllMessages(@PathVariable Long id, Authentication authentication) {
        User messageTo = userRepository.findUsersById(id);
        User messageFrom = userRepository.findUserByUserName(authentication.getName());

        //find all messages query the query looks awful, but we need to create a list of all messages between these two
        // users, which is refreshed after each message is sent (all incoming and outgoing messages are displayed)

        List<Message> listOfAllMessages = messageRepository.findAllMessages(messageTo.getId(), messageFrom.getUserName(),
                messageFrom.getId(), messageTo.getUserName());

        if (listOfAllMessages.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("No messages yet.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(messageService.showAllMessages(listOfAllMessages));
        }
    }

    //this endpoint sends a message to the selected user. The selected user is identified by his ID.
    @PostMapping("send/{id}")
    public ResponseEntity sendMessage(@PathVariable Long id,
                                      @RequestBody MessageBodyDTO message,
                                      Authentication authentication) {

        User user = userRepository.findUserByUserName(authentication.getName());

        if (!userRepository.findUserById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this username not found!");
        } else if (!userRepository.findUserById(id).get().isOnline()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is offline!");
        } else if (message.getMessage().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing message!");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(
                    userRepository.findUsersById(id), message.getMessage(), user));
        }
    }
}
