package models.registration.response;

public record SuccessfulRegistrationResponseModel(Integer id, String username, String firstName, String lastName, String email, String password, String remoteAddr) {

}
