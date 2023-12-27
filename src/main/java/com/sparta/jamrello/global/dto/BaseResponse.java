package com.sparta.jamrello.global.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.jamrello.global.constant.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BaseResponse<T> {

    private final String msg;
    private final Integer statusCode;
    private final T data;

    public static <T> BaseResponse<T> of(String msg, Integer statusCode, T data) {
        return new BaseResponse<>(msg, statusCode, data);
    }

    public static <T> BaseResponse<T> of(ResponseCode responseCode, T data) {
        return new BaseResponse<>(
                responseCode.getMessage(),
                responseCode.getHttpStatus(),
                data
        );
    }

    @JsonCreator  // Jackson 역직렬화에 사용할 생성자로 지정
    public BaseResponse(
        @JsonProperty("msg") String msg,  // JSON의 "msg" 키와 매핑
        @JsonProperty("statusCode") Integer statusCode,  // JSON의 "statusCode" 키와 매핑
        @JsonProperty("data") T data) {  // JSON의 "data" 키와 매핑
        this.msg = msg;
        this.statusCode = statusCode;
        this.data = data;
    }


}
