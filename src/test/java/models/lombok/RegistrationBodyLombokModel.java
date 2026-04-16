package models.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor -- если нужен конструктор
public class RegistrationBodyLombokModel {
    String username;
    String password;

}
