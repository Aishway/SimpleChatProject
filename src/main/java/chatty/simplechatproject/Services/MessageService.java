package chatty.simplechatproject.Services;

import chatty.simplechatproject.Models.DTO.MessageResponseDTO;
import chatty.simplechatproject.Models.Message;
import chatty.simplechatproject.Models.User;

import java.util.List;

public interface MessageService {

    List<MessageResponseDTO> showAllMessages(List<Message> list);

    List<MessageResponseDTO> sendMessage(User user, String message, User me);
}
