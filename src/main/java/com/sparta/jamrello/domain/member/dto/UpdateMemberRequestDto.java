package com.sparta.jamrello.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UpdateMemberRequestDto (

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "^.{4,16}$", message = "비밀번호는 4자, 16자이하 이여야합니다.")
    String password,
    @NotBlank(message = "닉네임은 필수항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "닉네임은 3자이상 8자이하 영대소문자, 숫자만 가능합니다.")
    String nickname,
    @NotBlank(message = "이메일은 필수항목입니다.")
    @Length(max = 255)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞게 작성해주세요.")
    String email
) {

}