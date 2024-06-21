package tests;

import io.restassured.response.Response;
import models.lombok.*;
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
                given(registerRequestSpec)
                .body(registerData)

                .when()
                .post()

                .then()
                .spec(registerResponseSpec)
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
                given(registerRequestSpec)
                .body(registerData)

                .when()
                .post()

                .then()
                .spec(registerMissingPasswordResponseSpec)
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

        Response response =  step("Make get unknown id request", () ->
                given(unknownResourceRequestSpec)

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
                given(usersListRequestSpec)
                .queryParam("page", pageNum)

                .when()
                .get()

                .then()
                .spec(usersListResponseSpec)
                .extract()
                .as(UsersListResponseBodyModel.class));


        step("Check response", ()-> {
            assertThat(response.getPage()).isEqualTo(1);
            assertThat(response.getPer_page()).isEqualTo(6);
            assertThat(response.getTotal()).isNotNull();
            assertThat(response.getTotal_pages()).isNotNull();
            assertThat(response.getData()).isNotNull();
        });
    }
}