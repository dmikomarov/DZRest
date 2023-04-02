import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class RestTests {
  @Test
  @DisplayName("Успешное создание пользователя")
  void createUserTest() {
    String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
    given()
            .log().uri()
            .contentType(JSON)
            .body(data)
            .when()
            .post("https://reqres.in/api/users")
            .then()
            .log().status()
            .log().body()
            .statusCode(201)
            .body("name", is("morpheus"))
            .body("job", is("leader"));
  }

  @Test
  @DisplayName("Неуспешное создание пользователя - кривой код ответа")
  void createUserTestFailed() {
    String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
    given()
            .log().uri()
            .contentType(JSON)
            .body(data)
            .when()
            .post("https://reqres.in/api/users")
            .then()
            .log().status()
            .log().body()
            .statusCode(202)
            .body("name", is("morpheus"))
            .body("job", is("leader"));
  }

  @Test
  @DisplayName("Получение 400, если отсутствуют требуемые данные")
  void badRequestCreateUserTest() {
    String data = "{ \"name\": , \"job\": \"leader\" }";

    given()
            .log().uri()
            .contentType(JSON)
            .body(data)
            .when()
            .post("https://reqres.in/api/users")
            .then()
            .log().status()
            .log().body()
            .statusCode(400);
  }

  @Test
  @DisplayName("Total pages = 2")
  void getTotalPages() {
    given()
            .log().uri()
            .when()
            .get("https://reqres.in/api/users?page=2")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .body("total_pages", is(2));

  }


  @Test
  @DisplayName("Пользователь не найден")
  void NotFoundTest() {
    given()
            .log().uri()
            .when()
            .get("https://reqres.in/api/users/23")
            .then()
            .log().status()
            .log().body()
            .statusCode(404);
  }


  @Test
  @DisplayName("Обновление имени пользователя")
  void usersNameUpdate() {
    String request_body = "{ \"name\": \"john\", \"job\": \"leader\" }";
    given()
            .log().all()
            .contentType(JSON)
            .body(request_body)
            .when()
            .put("https://reqres.in/api/users/2")
            .then()
            .log().all()
            .statusCode(200)
            .body("name", is("john"));
  }
}
