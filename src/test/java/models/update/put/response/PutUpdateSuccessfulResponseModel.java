package models.update.put.response;

public record PutUpdateSuccessfulResponseModel(String id,
                                               String username,
                                               String firstName,
                                               String lastName,
                                               String email,
                                               String remoteAddr) {
}
