package models.put;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.UsersMethodsData;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponsePut {
    @Builder.Default
    private String name = UsersMethodsData.UPDATED_USER_NAME_FOR_PUT;

    @Builder.Default
    private String job = UsersMethodsData.UPDATED_USER_JOB_FOR_PUT;

    @Builder.Default
    OffsetDateTime updatedAt = OffsetDateTime.now();
}
