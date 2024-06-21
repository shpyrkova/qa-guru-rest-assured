package tests;

import io.restassured.response.Response;
import models.lombok.CreateUserRequestBodyModel;
import models.lombok.CreateUserResponseBodyModel;
import models.lombok.RegisterRequestBodyModel;
import models.lombok.RegisterResponseBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static specs.CreateUserSpec.createUserRequestSpec;
import static specs.CreateUserSpec.createUserResponseSpec;
import static specs.RegisterSpec.*;
import static specs.UnknownResourceSpec.unknownResourceRequestSpec;
import static specs.UnknownResourceSpec.unknownResourceResponseSpec;
import static specs.UsersListSpec.usersListRequestSpec;
import static specs.UsersListSpec.usersListResponseSpec;

public class ReqTests extends TestBase {

    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulCreateUserTest() {

        CreateUserRequestBodyModel userData = new CreateUserRequestBodyModel();
        userData.setName("daria");
        userData.setJob("leader");

        CreateUserResponseBodyModel response = step("Make create user request", () ->
                given(createUserRequestSpec)
                .body(userData)

                .when()
                .post()

                .then()
                .spec(createUserResponseSpec)
                .extract()
                .as(CreateUserResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.toString()).contains("name=daria");
            assertThat(response.toString()).contains("job=leader");
            assertThat(response.toString()).contains("id");
            assertThat(response.toString()).contains("createdAt");
        });
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void successfulRegisterTest() {

        RegisterRequestBodyModel registerData = new RegisterRequestBodyModel();
        registerData.setEmail("eve.holt@reqres.in");
        registerData.setPassword("pistol");

        RegisterResponseBodyModel response = step("Make register request", () ->
                given(registerRequestSpec)
                .body(registerData)

                .when()
                .post()

                .then()
                .spec(registerResponseSpec)
                .extract()
                .as(RegisterResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.toString()).contains("id");
            assertThat(response.toString()).contains("token");
        });
    }

    @Test
    @DisplayName("Попытка регистрации пользователя без пароля")
    void unsuccessfulRegisterTest() {

        RegisterRequestBodyModel registerData = new RegisterRequestBodyModel();
        registerData.setEmail("eve.holt@reqres.in");

        RegisterResponseBodyModel response = step("Make register request without password", () ->
                given(registerRequestSpec)
                .body(registerData)

                .when()
                .post()

                .then()
                .spec(registerMissingPasswordResponseSpec)
                .extract()
                .as(RegisterResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.toString()).contains("error=Missing password");
        });

    }

    @Test
    @DisplayName("Попытка получения ресурса с несуществующим id")
    void getNotFoundResourceTest() {

        int id = 467898908;

        Response response = given(unknownResourceRequestSpec)

                .when()
                .get("/unknown/" + id)

                .then()
                .spec(unknownResourceResponseSpec)
                .extract()
                .response();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Успешное получение списка пользователей")
    void successfulListUsersTest() {

        int pageNum = 1;

        Response response = given(usersListRequestSpec)
                .queryParam("page", pageNum)

                .when()
                .get()

                .then()
                .spec(usersListResponseSpec)
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).contains("\"id\":1,\"email\":\"george.bluth@reqres.in\"");
    }
}