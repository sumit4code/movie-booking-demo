package com.intuit.craft.theater.helper;

import com.intuit.craft.theater.domain.Schedule;
import com.intuit.craft.theater.domain.SubEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateWiseEventHelper {

    public static List<SubEvent> createSubEvent(Date startDate, Date endDate, List<Schedule> scheduleList) {
        List<SubEvent> subEventList = new ArrayList<>();
        LocalDate eventStartDate = ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.of("UTC")).toLocalDate();
        LocalDate eventEndDate = ZonedDateTime.ofInstant(endDate.toInstant(), ZoneId.of("UTC")).toLocalDate();
        while (!eventStartDate.isAfter(eventEndDate)) {
            for (LocalDate date = eventStartDate; date.isBefore(eventEndDate); date = date.plusDays(1)) {
                for (Schedule schedule : scheduleList) {
                    SubEvent subEvent = SubEvent.builder().subEventId(UUID.randomUUID().toString())
                            .showStartTime(date.atTime(schedule.getStartTime()))
                            .showEndTime(date.atTime(schedule.getEndTime()))
                            .build();
                    subEventList.add(subEvent);
                }
            }
        }
        return subEventList;
    }
}
