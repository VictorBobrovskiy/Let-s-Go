package com.digsol.main.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;

    private Long event;

    private Long requester;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String created;
}