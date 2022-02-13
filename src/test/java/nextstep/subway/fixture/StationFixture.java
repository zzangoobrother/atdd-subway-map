package nextstep.subway.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.httpresponse.Response.post;

public class StationFixture {

    public static final String 강남역_이름 = "강남역";
    public static final String 역삼역_이름 = "역삼역";
    public static final String 구로역_이름 = "구로역";
    public static final String 대림역_이름 = "대림역";

    public static ExtractableResponse<Response> 역_생성(String name) {
        Map<String, String> station = station(name);
        return post(station, "/stations");
    }

    private static Map<String, String> station(String name) {
        Map<String, String> station = new HashMap<>();
        station.put("name", name);
        return station;
    }

    public static Long stationId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }
}