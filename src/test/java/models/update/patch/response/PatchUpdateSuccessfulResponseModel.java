package models.update.patch.response;

public record PatchUpdateSuccessfulResponseModel(String id,
                                                 String username,
                                                 String firstName,
                                                 String lastName,
                                                 String email,
                                                 String remoteAddr) {
}
