package chatty.simplechatproject.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String messageFromUser;
    private String message;
    private String date;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    public void setDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formation = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date = date.format(formation);
    }

}
