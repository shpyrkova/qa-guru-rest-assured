import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.*;

public class ReqTests extends TestBase {

    @Test
    @DisplayName("Успешное получение списка пользователей")
    void successfulListUsersTest() {

        Response response = given()
                .log().uri()
                .when()
                .get("/users?page=1")
                .then()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).contains("\"id\":1,\"email\":\"george.bluth@reqres.in\"");
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulCreateUserTest() {
        String userData = "{\"name\": \"daria\", \"job\": \"leader\"}";

        Response response = given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .log().body()

                .when()
                .post("/users")
                .then()
                .log().body()
                .extract()
                .response();

        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.getBody().asString()).contains("\"name\":\"daria\"");
        assertThat(response.getBody().asString()).contains("\"job\":\"leader\"");
        assertThat(response.getBody().asString()).contains("id");
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void successfulRegisterTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        Response response = given()
                .body(registerData)
                .contentType(JSON)
                .log().uri()
                .log().body()

                .when()
                .post("/register")
                .then()
                .log().body()
                .extract()
                .response();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).contains("id");
        assertThat(response.getBody().asString()).contains("token");
    }

    @Test
    @DisplayName("Попытка регистрации пользователя без пароля")
    void unsuccessfulRegisterTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\"}";

        Response response = given()
                .body(registerData)
                .contentType(JSON)
                .log().uri()
                .log().body()

                .when()
                .post("/register")
                .then()
                .log().body()
                .extract()
                .response();

        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(response.getBody().asString()).contains("\"error\":\"Missing password\"");
    }

    @Test
    @DisplayName("Попытка получения ресурса с несуществующим id")
    void getNotFoundResourceTest() {

        int id = 467898908;

        Response response = given()
                .log().uri()
                .log().body()

                .when()
                .get("/unknown/" + id)
                .then()
                .extract()
                .response();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }
}