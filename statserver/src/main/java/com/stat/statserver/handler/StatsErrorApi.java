package com.stat.statserver.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Schema(description = "В StatsErrorApi вовзращается информация о возникшей ошибке")
public class StatsErrorApi {

    @Schema(description = "Внутренний код ошибки")
    Integer errorCode;
    @Schema(description = "Сообщение(-я) о произошедшей ошибке(-ах)")
    String message;
    @Schema(description = "HttpStatus")
    HttpStatus status;

}
