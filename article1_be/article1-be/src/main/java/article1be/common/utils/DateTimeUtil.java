package article1be.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// DateTime 관련 유틸 클래스
public class DateTimeUtil {

    // 분까지만 포매팅
    private static final DateTimeFormatter mFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 문자열을 LocalDateTime으로 변환
    public static LocalDateTime parseDateTime(String inputTime) {
        return LocalDateTime.parse(inputTime, mFormatter);
    }

    // LocalDateTime을 문자열로 변환
    public static String formatToString(LocalDateTime localDateTime) {
        return localDateTime.format(mFormatter);
    }
}
