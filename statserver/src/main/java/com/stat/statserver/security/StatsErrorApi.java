package com.stat.statserver.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsErrorApi {

    Integer errorCode;
    String message;
    HttpStatus status;

}
