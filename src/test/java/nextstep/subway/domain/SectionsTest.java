package nextstep.subway.domain;

import nextstep.subway.exception.SectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {
    private static int DEFAULT_DISTANCE = 5;

    @DisplayName("구간 등록 시에 상행역이 아닌 하행역 등록은 예외")
    @Test
    void matchDownStationException() {
        // given
        Station station1 = createStation("강남역");
        Station station2 = createStation("역삼역");
        Station station3 = createStation("잠실역");
        Station station4 = createStation("잠실새내역");

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));
        Section section = createSection(station4, station4);

        // when, then
        assertThatThrownBy(() -> sections.validationSectionStation(section.getUpStation(), section.getDownStation()))
                .isInstanceOf(SectionException.class)
                .hasMessage("하행역만 상행역으로 등록될 수 있습니다.");
    }


    @DisplayName("이미 구간에 등록된 역은 하행역 등록 시 예외")
    @Test
    void matchAllStationException() {
        // given
        Station station1 = createStation("강남역");
        Station station2 = createStation("역삼역");
        Station station3 = createStation("잠실역");

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));
        Section section = createSection(station3, station1);

        // when, then
        assertThatThrownBy(() -> sections.validationSectionStation(section.getUpStation(), section.getDownStation()))
                .isInstanceOf(SectionException.class)
                .hasMessage("이미 구간에 등록되어 있습니다.");
    }

    private Line createLine() {
        return new Line("2호선", "bg-green-700");
    }

    private Station createStation(String name) {
        return new Station(name);
    }

    private Section createSection(Station upStation, Station downStation) {
        return new Section(createLine(), upStation, downStation, DEFAULT_DISTANCE);
    }

    private Sections createSections(Section ...sections) {
        return new Sections(Arrays.asList(sections));
    }
}
