package models.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import models.UsersMethodsData;

@Getter
@AllArgsConstructor
@Builder
public class UserRequestPost {
    @Builder.Default
    private String name = UsersMethodsData.NEW_USER_NAME_FOR_POST;

    @Builder.Default
    private String job = UsersMethodsData.NEW_USER_JOB_FOR_POST;
}
