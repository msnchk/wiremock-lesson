package data;

import java.util.stream.Stream;

public class UsersData {
    public static final UserModel EXISTING_USER = new UserModel(
            2,
            "Janet",
            "Weaver",
            "janet.weaver@reqres.in",
            ""
    );

    public static final UserModel NON_EXISTENT_USER = new UserModel(
            23,
            "",
            "",
            "",
            ""
    );

    public static Stream<UserModel> newUsersProvider() {
        return Stream.of(
                new UserModel(0, "Ivan", "Ivanov", "ivanov@mail.ru", "manager"),
                new UserModel(0, "Anna", "Petrova", "petrova@gmail.com", "agent")
        );
    }
}
