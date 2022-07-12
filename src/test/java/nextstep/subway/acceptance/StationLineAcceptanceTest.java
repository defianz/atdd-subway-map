package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.StaticMethodUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
public class StationLineAcceptanceTest extends AcceptanceTest{

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // When
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        ExtractableResponse<Response> response = createStationLine(stationLineName, stationLineColor, upStationId, downStationId);
        Long createdStationLineId = extractIdInResponse(response);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getAllStationLines());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(ids).containsAnyOf(createdStationLineId);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선의 목록을 조회한다.")
    @Test
    void getStationLines() {
        // Given
        String stationLineName1 = "신분당선";
        String stationLineColor1 = "bg-red-600";
        String stationLineName2 = "신분당선2";
        String stationLineColor2 = "bg-red-6002";

        String upStationName1 = "지하철역";
        String downStationName1 = "새로운지하철역";
        String upStationName2 = "지하철역2";
        String downStationName2 = "새로운지하철역2";

        Long upStationId1 = extractIdInResponse(createStationWithName(upStationName1));
        Long downStationId1 = extractIdInResponse(createStationWithName(downStationName1));
        Long upStationId2 = extractIdInResponse(createStationWithName(upStationName2));
        Long downStationId2 = extractIdInResponse(createStationWithName(downStationName2));

        Long createdStationLineId1 = extractIdInResponse(createStationLine(stationLineName1, stationLineColor1, upStationId1, downStationId1));
        Long createdStationLineId2 = extractIdInResponse(createStationLine(stationLineName2, stationLineColor2, upStationId2, downStationId2));

        // When
        ExtractableResponse<Response> allStationLines = getAllStationLines();

        // Then
        List<Long> ids = extractIdsInListTypeResponse(allStationLines);
        assertThat(allStationLines.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ids).hasSize(2);
        assertThat(ids).contains(createdStationLineId1, createdStationLineId2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getStationLine() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        ExtractableResponse<Response> response = getStationLineWithId(createdStationLineId);

        // Then
        Long responseStationLineId = extractIdInResponse(response);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseStationLineId).isEqualTo(createdStationLineId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        String updateName = "다른분당선";
        String updateColor = "bg-red-601";

        Map<Object, Object> params = new HashMap<>();
        params.put("name", updateName);
        params.put("color", updateColor);

        ExtractableResponse response = updateStationLine(createdStationLineId, params);

        // Then
        JsonPath jsonPath = getStationLineWithId(createdStationLineId).jsonPath();
        String updatedName = jsonPath.getString("name");
        String updatedColor = jsonPath.getString("color");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updatedName).isEqualTo(updateName);
        assertThat(updatedColor).isEqualTo(updateColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteStationLines() {
        // Given
        String stationLineName = "신분당선";
        String stationLineColor = "bg-red-600";

        String upStationName = "지하철역";
        String downStationName = "새로운지하철역";

        Long upStationId = extractIdInResponse(createStationWithName(upStationName));
        Long downStationId = extractIdInResponse(createStationWithName(downStationName));

        Long createdStationLineId = extractIdInResponse(createStationLine(stationLineName, stationLineColor, upStationId, downStationId));

        // When
        ExtractableResponse<Response> deleteResponse = deleteStationLineWithId(createdStationLineId);

        // Then
        List<Long> ids = extractIdsInListTypeResponse(getAllStationLines());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(ids).doesNotContain(createdStationLineId);
    }

    private ExtractableResponse<Response> createStationLine(String stationLineName, String stationLineColor, Long upStationId, Long downStationId
    ) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", stationLineName);
        params.put("color", stationLineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);

        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> getAllStationLines() {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> getStationLineWithId(Long id) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .get("/lines/{id}", id)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse updateStationLine(Long lineId, Map<Object, Object> params) {
        return RestAssured.given()
                .log()
                .all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{lineId}", lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStationLineWithId(Long createdStationLineId) {
        return RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/lines/{id}", createdStationLineId)
                .then()
                .log()
                .all()
                .extract();
    }
}