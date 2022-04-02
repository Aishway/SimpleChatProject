package chatty.simplechatproject.Repositories;

import chatty.simplechatproject.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM chatty.message JOIN chatty.user ON message_from_user = user_name \n" +
            "WHERE (user_id = ? AND message_from_user = ?) OR (user_id = ?  AND message_from_user = ?) ",
            nativeQuery = true)
    List<Message> findAllMessages(Long id, String fromMe, Long mineId, String fromUser);
}
