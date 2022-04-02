package chatty.simplechatproject.Services;

import chatty.simplechatproject.Models.DTO.MessageResponseDTO;
import chatty.simplechatproject.Models.Message;
import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageResponseDTO> showAllMessages(List<Message> list) {
        List<MessageResponseDTO> dtos = new ArrayList<>();
        // a DTO was created for the messages so that the response contained only the required attributes
        list.forEach(e -> dtos.add(new MessageResponseDTO(e.getMessageFromUser(), e.getMessage(), e.getDate())));
        return dtos;
    }

    @Override
    public List<MessageResponseDTO> sendMessage(User user, String message, User me) {
        Message addMessage = new Message();
        addMessage.setMessage(message);
        addMessage.setMessageFromUser(me.getUserName());
        addMessage.setUser(user);
        addMessage.setDate();
        messageRepository.save(addMessage);
        // method showAllMessages refresh messages, therefore the current messages are always displayed after sending
        return showAllMessages(messageRepository.findAllMessages(user.getId(), me.getUserName(), me.getId(),
                user.getUserName()));
    }
}
