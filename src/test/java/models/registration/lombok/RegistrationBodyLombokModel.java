package models.registration.lombok;

import lombok.Data;

@Data
//@AllArgsConstructor -- если нужен конструктор
public class RegistrationBodyLombokModel {
    String username;
    String password;

}
