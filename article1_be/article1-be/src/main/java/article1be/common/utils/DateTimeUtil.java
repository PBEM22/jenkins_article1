package article1be.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// DateTime 관련 유틸 클래스
public class DateTimeUtil {

    // 분까지만 포매팅
    private static final DateTimeFormatter mFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 문자열을 LocalDateTime으로 변환
    public static LocalDateTime stringParseToLocalDateTime(String inputTime) {
        return LocalDateTime.parse(inputTime, mFormatter);
    }

    // LocalDateTime을 문자열로 변환
    public static String formatToString(LocalDateTime localDateTime) {
        return localDateTime.format(mFormatter);
    }

    // dt를 우리나라 시간으로 적용해서 LocalDateTime으로 변환
    public static LocalDateTime dtParseToLocalDateTime(long dt) {

        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        return Instant.ofEpochSecond(dt)
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
    }
}
