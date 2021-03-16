package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class SectionRequestStep {

    public static ExtractableResponse<Response> 지하철_구간_삭제요청(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .param("stationId", stationId)
                .when()
                .delete("/lines/{lineId}/sections/", lineId)

                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_등록요청(Long lineId, LineRequest line) {
        return RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static Long 지하철_구간_등록되어_있음(Long lineId, LineRequest line) {
        return 지하철_구간_등록요청(lineId, line).as(LineResponse.class).getId();
    }
}