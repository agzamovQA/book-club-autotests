package models.update.patch.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUpdateBodyModel {
    String username;
    String firstName;
    String lastName;
    String email;
}
