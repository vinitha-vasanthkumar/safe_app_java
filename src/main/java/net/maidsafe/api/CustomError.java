package net.maidsafe.api;

import net.maidsafe.utils.FfiConstant;

public enum CustomError {

    CONTAINERS_CAN_NOT_BE_EMPTY(CustomError.BASE + 1, "Containers can not be empty"),
    NOT_VALID_AUTH_URI(CustomError.BASE + 2, "Not a valid URI for creating a client"),
    INVALID_SIGN_PUB_KEY_SIZE(CustomError.BASE + 3, String.format("Invalid argument. Public key size must be %d long", FfiConstant.SIGN_PUBLICKEYBYTES)),
    INVALID_BOX_PUB_KEY_SIZE(CustomError.BASE + 4, String.format("Invalid argument. Public key size must be %d long", FfiConstant.BOX_PUBLICKEYBYTES)),
    ARG_CAN_NOT_BE_NULL(CustomError.BASE + 5, "Argument cannot be null");

    public static final int BASE = 100;
    private final String description;
    private final int code;

    CustomError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getErrorCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

}
