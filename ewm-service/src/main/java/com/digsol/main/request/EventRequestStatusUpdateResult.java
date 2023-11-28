package com.digsol.main.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class EventRequestStatusUpdateResult {

    private final Collection<RequestDto> confirmedRequests = new ArrayList<>();

    private final Collection<RequestDto> rejectedRequests = new ArrayList<>();

}