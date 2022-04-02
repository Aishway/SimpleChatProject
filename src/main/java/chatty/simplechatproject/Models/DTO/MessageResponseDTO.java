package chatty.simplechatproject.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    private String messageFrom;
    private String message;
    private String date;
}
