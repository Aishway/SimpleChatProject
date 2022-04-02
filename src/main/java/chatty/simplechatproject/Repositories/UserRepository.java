package chatty.simplechatproject.Repositories;

import chatty.simplechatproject.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserName(String username);

    User findUsersById(Long id);

    Optional<User> findByUserName(String username);

    Optional<User> findUserById(Long id);

    @Query(value = "SELECT * FROM chatty.user WHERE is_online = 1", nativeQuery = true)
    List<User> findOnlineUsers();


}
