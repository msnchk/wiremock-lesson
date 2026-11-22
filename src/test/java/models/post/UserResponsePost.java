package models.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.UsersMethodsData;

import java.time.OffsetDateTime;
import java.util.Random;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponsePost {
    @Builder.Default
    private String name = UsersMethodsData.NEW_USER_NAME_FOR_POST;

    @Builder.Default
    private String job = UsersMethodsData.NEW_USER_JOB_FOR_POST;

    @Builder.Default
    private int id = new Random().nextInt(100) + 1;

    @Builder.Default
    OffsetDateTime createdAt = OffsetDateTime.now();
}
