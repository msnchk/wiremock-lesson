package models.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.UsersMethodsData;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseGet {
    @Builder.Default
    private Data data = Data.builder().build();

    @Builder.Default
    private Support support = Support.builder().build();

    @Builder.Default
    private Meta _meta = Meta.builder().build();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Data {
        @Builder.Default
        private int id = UsersMethodsData.EXISTING_USER_ID_FOR_GET_PUT_DELETE;

        @Builder.Default
        private String email = UsersMethodsData.EXISTING_USER_EMAIL_FOR_GET;

        @Builder.Default
        private String first_name = UsersMethodsData.EXISTING_USER_FIRSTNAME_FOR_GET;

        @Builder.Default
        private String last_name = UsersMethodsData.EXISTING_USER_LASTNAME_FOR_GET;

        @Builder.Default
        private String avatar = UsersMethodsData.EXISTING_USER_AVATAR_LINK_FOR_GET;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Support {
        @Builder.Default
        private String url = null;

        @Builder.Default
        private String text = null;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Meta {
        @Builder.Default
        private String powered_by = null;

        @Builder.Default
        private String upgrade_url = null;

        @Builder.Default
        private String docs_url = null;

        @Builder.Default
        private String template_gallery = null;

        @Builder.Default
        private String message = null;

        @Builder.Default
        private List<String> features = null;

        @Builder.Default
        private String upgrade_cta = null;
    }
}
