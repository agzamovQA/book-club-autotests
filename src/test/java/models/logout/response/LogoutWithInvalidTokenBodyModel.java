package models.logout.response;

public record LogoutWithInvalidTokenBodyModel(String detail, String code) {
}
