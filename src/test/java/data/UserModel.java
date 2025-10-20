package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserModel {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String job;
}
