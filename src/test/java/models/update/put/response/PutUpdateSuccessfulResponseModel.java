package models.update.put.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutUpdateSuccessfulResponseModel {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String remoteAddr;
}
