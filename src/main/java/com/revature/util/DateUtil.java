package com.revature.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class DateUtil {
    public static Date getSqlDate() {
        LocalDateTime now = LocalDateTime.now();
        return Date.valueOf(now.toLocalDate());
    }

}
