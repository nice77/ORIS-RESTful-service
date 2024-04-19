package org.semester.dto.authServiceDto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class AuthorizeRequest {
    private String email;
    private String password;
}
