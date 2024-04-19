package org.semester.dto.authServiceDto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class RefreshRequest {
    private String refresh;
}
