package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.fixture.TLine.신분당선;
import static nextstep.subway.steps.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.steps.SectionSteps.*;
import static nextstep.subway.steps.StationSteps.지하철_역_생성_요청;

@DisplayName("지하철 노선 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        지하철_역_생성_요청("역1");
        지하철_역_생성_요청("역2");
        지하철_역_생성_요청("역3");
        지하철_역_생성_요청("역4");
    }

    /**
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * Given 지하철 노선 생성을 요청 하고
     * When 생성 노선 하행역과 이어지는 구간 등록을 요청하면
     * Then 지하철 노선 구간 등록이 성공한다.
     */
    @DisplayName("구간 등록")
    @Test
    void addSection() {
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(2L, 3L, 10);

        // then
        구간_등록_성공(response);
    }

    /**
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * Given 지하철 노선 생성을 요청 하고
     * When 노선에 존재하는 역을 하행역으로 갖는 구간 등록을 요청하면
     * Then 지하철 노선 구간 등록이 실패한다.
     */
    @DisplayName("구간 등록 실패 - 존재하는 역을 신규 구간 하행역으로 등록")
    @Test
    void addSectionAlreadyExistStation() {
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(3L, 1L, 10);

        // then
        구간_등록_실패(response);
    }

    /**
     * 지하철 노선에서 구간을 삭제한다.
     * Given 지하철 노선 생성을 요청 하고
     * When 생성 노선 하행역과 이어지지 않는 구간 등록을 요청하면
     * Then 지하철 노선 구간 등록이 실패한다.
     */
    @DisplayName("구간 삭제")
    @Test
    void removeSection() {
        // given
        지하철_노선_생성_요청(신분당선);
        지하철_구간_등록_요청(2L, 3L, 10);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(3L);

        // then
        구간_삭제_성공(response);
    }

    /**
     * 노선에 구간이 한 개 남은 경우 실패한다.
     * Given 지하철 노선 생성을 요청 하고
     * When 구간 제거를 요청하면
     * Then 지하철 노선 구간 제거가 실패한다.
     */
    @DisplayName("구간 삭제 실패 - 노선에 구간이 한 개 남은 경우")
    @Test
    void removeSectionFail() {
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(2L);

        // then
        구간_삭제_실패(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * AND 구간 등록을 요청한 뒤
     * When 마지막 구간이 아닌 구간 제거를 요청하면
     * Then 지하철 노선 구간 제거가 실패한다.
     */
    @DisplayName("구간 삭제 실패 - 마지막 구간이 아닌 경우")
    @Test
    void removeSectionFailNotLastSection() {
        // given
        지하철_노선_생성_요청(신분당선);
        지하철_구간_등록_요청(2L, 3L, 10);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(1L);

        // then
        구간_삭제_실패(response);
    }
}