package models.logout.response;

import java.util.List;

public record LogoutWithoutTokenBodyModel(List<String> refresh) {
}
