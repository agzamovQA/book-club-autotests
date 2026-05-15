package models.registration.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulRegistrationResponseModel {
    Integer id;
    String username;
    String firstName;
    String lastName;
    String email;
    String password;
    String remoteAddr;
}
