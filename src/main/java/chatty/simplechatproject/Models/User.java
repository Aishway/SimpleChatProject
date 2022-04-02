package chatty.simplechatproject.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private boolean isOnline;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> list;
}
