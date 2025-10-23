package models.put;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import models.UsersMethodsData;

@Getter
@AllArgsConstructor
@Builder
public class UserRequestPut {
    @Builder.Default
    @JsonIgnore
    private int id = UsersMethodsData.EXISTING_USER_ID_FOR_GET_PUT_DELETE;

    @Builder.Default
    private String name = UsersMethodsData.UPDATED_USER_NAME_FOR_PUT;

    @Builder.Default
    private String job = UsersMethodsData.UPDATED_USER_JOB_FOR_PUT;
}
