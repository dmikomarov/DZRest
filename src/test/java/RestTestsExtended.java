import model.CreatedUsers;
import model.RegisterBody;
import model.RegisterResponseErrorModel;
import model.UpdateResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static specs.Specs.*;

public class RestTestsExtended {
  @Test
  @DisplayName("Успешное создание пользователя")
  void createUserTest() {
    CreatedUsers createUserBody = new CreatedUsers();
    createUserBody.setName("morpheus");
    createUserBody.setJob("leader");
    CreatedUsers createUser =
            step("Создание пользователя", () ->
                    given(baseRequestSpec)
                            .body(createUserBody)
                            .when()
                            .post("/users")
                            .then()
                            .spec(createResponseSpec)
                            .extract().as(CreatedUsers.class));
    assertThat(createUser.getName()).isEqualTo("morpheus");
    assertThat(createUser.getJob()).isEqualTo("leader");
  }


  @Test
  @DisplayName("REGISTER - UNSUCCESSFUL - 400 ")
  void RegisterUnsuccessfulUserTest() {
    RegisterBody registerBody = new RegisterBody();
    registerBody.setPassword("pistol");
    RegisterResponseErrorModel registerResponseError =
            step("Post register data", () ->
                    given(baseRequestSpec)
                            .body(registerBody)
                            .when()
                            .post("/register")
                            .then()
                            .spec(ResponseSpecPsw400)
                            .extract().as(RegisterResponseErrorModel.class));
    step("Verify error", () -> {
      assertThat(registerResponseError.getError()).isEqualTo("Missing email or username");
    });
  }

  @Test
  @DisplayName("Total pages = 2")
  void getTotalPages() {
    step("Check 'Total pages'", () -> given(baseRequestSpec)
            .when()
            .get("/users?page=2")
            .then()
            .spec(totalPageResponseSpec));

  }


  @Test
  @DisplayName("Пользователь не найден")
  void notFoundTest() {
    step("Check user's", () -> given(baseRequestSpec)
            .when()
            .get("/users/23")
            .then()
            .spec(ResponseSpecPsw404));
  }


  @Test
  @DisplayName("Обновление имени пользователя")
  void usersNameUpdate() {
    String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
    CreatedUsers updateBody = new CreatedUsers();
    updateBody.setName("john");
    updateBody.setJob("leader");
    UpdateResponseModel updateNameUser = step("Update user's  place of work", () ->
            given(baseRequestSpec)
                    .body(updateBody)
                    .when()
                    .put("/users/2")
                    .then()
                    .spec(updateResponseSpec)
                    .extract().as(UpdateResponseModel.class));

    step("Checking that the place of work has been edited", () -> {
      assertThat(updateNameUser.getUpdatedAt()).contains(dateTime);
    });
  }
}
