package models.update.patch.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUpdateSuccessfulResponseModel {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String remoteAddr;
}
