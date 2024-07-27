package com.exciting.vvue.schedule.exception;

import com.exciting.vvue.common.exception.NotFoundException;

public class ScheduleNotFoundException extends NotFoundException {
    public ScheduleNotFoundException(String reason){ super(reason); };
}
