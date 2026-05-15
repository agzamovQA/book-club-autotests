package models.logout.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutWithInvalidTokenBodyModel {
    String detail;
    String code;
}
