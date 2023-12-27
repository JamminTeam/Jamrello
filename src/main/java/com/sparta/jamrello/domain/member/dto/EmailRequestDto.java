package com.sparta.jamrello.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public record EmailRequestDto (
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Length(max = 255)
    @Email
    String email

) {

}
