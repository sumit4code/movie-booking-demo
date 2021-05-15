package com.intuit.craft.theater.helper;

import com.intuit.craft.theater.domain.Schedule;
import com.intuit.craft.theater.domain.SubEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateWiseEventHelper {

    public static List<SubEvent> createSubEvent(Date startDate, Date endDate, List<Schedule> scheduleList) {
        log.debug("startDate: {}, endDate: {} scheduleList: {}", startDate, endDate, scheduleList);
        List<SubEvent> subEventList = new ArrayList<>();
        LocalDate eventStartDate = ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.of("UTC")).toLocalDate();
        LocalDate eventEndDate = ZonedDateTime.ofInstant(endDate.toInstant(), ZoneId.of("UTC")).toLocalDate();
        for (LocalDate date = eventStartDate; date.isBefore(eventEndDate); date = date.plusDays(1)) {
            log.debug("Date {}", date);
            for (Schedule schedule : scheduleList) {
                log.debug("schedule : {}", schedule);
                LocalDateTime showStartTime = date.atTime(schedule.getStartTime());
                LocalDateTime showEndTime = date.atTime(schedule.getEndTime());
                SubEvent subEvent = SubEvent.builder().subEventId(UUID.randomUUID().toString())
                        .showStartTime(showStartTime)
                        .showEndTime(showEndTime)
                        .build();
                subEventList.add(subEvent);
            }
        }
        return subEventList;
    }
}
