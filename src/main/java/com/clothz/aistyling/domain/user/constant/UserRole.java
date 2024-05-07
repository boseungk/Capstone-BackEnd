package com.clothz.aistyling.domain.user.constant;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public enum UserRole {
    USER("USER"),
    GUEST("GUEST");
    private final String type;

    UserRole(final String type) {
        this.type = type;
    }

    public static UserRole fromString(final String type) {
        return Arrays.stream(values())
                .filter(UserRole -> UserRole.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: %s".formatted(type)));
    }

    public String getType() {
        return type;
    }

    public class MemberRoleToStringConverter implements AttributeConverter<UserRole, String> {

        @Override
        public String convertToDatabaseColumn(final UserRole attribute) {
            return attribute.getType();
        }

        @Override
        public UserRole convertToEntityAttribute(final String dbData) {
            return fromString(dbData);
        }
    }
}
