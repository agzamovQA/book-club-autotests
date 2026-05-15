package models.update.put.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutUpdateBodyModel {
    String username;
    String firstName;
    String lastName;
    String email;
}
