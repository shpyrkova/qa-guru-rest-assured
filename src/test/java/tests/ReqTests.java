package tests;

import io.restassured.response.Response;
import models.lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static specs.RequestResponseSpecs.*;

public class ReqTests extends TestBase {

    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulCreateUserTest() {
        CreateUserRequestBodyModel userData = new CreateUserRequestBodyModel();
        userData.setName("daria");
        userData.setJob("leader");

        CreateUserResponseBodyModel response = step("Make create user request", () ->
                given(requestSpec)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .spec(createdResponseSpec)
                .extract()
                .as(CreateUserResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.getName()).isEqualTo("daria");
            assertThat(response.getJob()).isEqualTo("leader");
            assertThat(response.getId()).isNotEmpty();
            assertThat(response.getCreatedAt()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void successfulRegisterTest() {
        RegisterRequestBodyModel registerData = new RegisterRequestBodyModel();
        registerData.setEmail("eve.holt@reqres.in");
        registerData.setPassword("pistol");

        RegisterResponseBodyModel response = step("Make register request", () ->
                given(requestSpec)
                .body(registerData)
                .when()
                .post("/register")
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(RegisterResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getToken()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Попытка регистрации пользователя без пароля")
    void unsuccessfulRegisterTest() {
        RegisterRequestBodyModel registerData = new RegisterRequestBodyModel();
        registerData.setEmail("eve.holt@reqres.in");

        RegisterResponseBodyModel response = step("Make register request without password", () ->
                given(requestSpec)
                .body(registerData)
                .when()
                .post("/register")
                .then()
                .spec(badRequestResponseSpec)
                .extract()
                .as(RegisterResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.getError()).isEqualTo("Missing password");
        });

    }

    @Test
    @DisplayName("Попытка получения ресурса с несуществующим id")
    void getNotFoundResourceTest() {
        int id = 467898908;

        Response response = step("Make get unknown id request", () ->
                given(requestSpec)
                .when()
                .get("/unknown/" + id)
                .then()
                .spec(unknownResourceResponseSpec)
                .extract()
                .response());

        step("Check response", ()->
            assertThat(response.getStatusCode()).isEqualTo(404)
        );
    }

    @Test
    @DisplayName("Успешное получение списка пользователей")
    void successfulListUsersTest() {
        int pageNum = 1;

        UsersListResponseBodyModel response = step("Make get users list request", () ->
                given(requestSpec)
                .queryParam("page", pageNum)
                .when()
                .get("/users")
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(UsersListResponseBodyModel.class));

        step("Check response", ()-> {
            assertThat(response.getPage()).isEqualTo(1);
            assertThat(response.getPerPage()).isEqualTo(6);
            assertThat(response.getTotal()).isNotNull();
            assertThat(response.getTotalPages()).isNotNull();
            assertThat(response.getData()).isNotNull();
        });
    }
}