package nextstep.subway.acceptance.requests;

import static nextstep.subway.acceptance.type.GeneralNameType.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineRequests {
    public static final String LOCATION = "Location";

    public static ExtractableResponse<Response> lineCreateRequest(String name, String color) {

        return RestAssured.given()
                .log()
                .all()
                .body(makeCreationAndUpdateRequestBody(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> readLineListRequest() {
        return RestAssured.given().log().all().when().get("/lines").then().log().all().extract();
    }

    public static ExtractableResponse<Response> specificLineReadRequest(Long lineId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/lines/{lineId}", lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> lineUpdateRequest(
            String uri, String name, String color) {

        return RestAssured.given()
                .log()
                .all()
                .body(makeCreationAndUpdateRequestBody(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then()
                .log()
                .all()
                .extract();
    }

    private static Map<String, String> makeCreationAndUpdateRequestBody(String name, String color) {
        Map<String, String> createRequest = new HashMap<>();
        createRequest.put(NAME.getType(), name);
        createRequest.put(COLOR.getType(), color);
        return createRequest;
    }
}