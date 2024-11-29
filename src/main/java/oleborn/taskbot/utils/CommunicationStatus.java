package oleborn.taskbot.utils;

public enum CommunicationStatus {

    BLOCKED,
    DEFAULT,

    // ---- input ----//

    INPUT_TEXT,
    INPUT_TITLE,
    INPUT_FRIEND,
    INPUT_GMT,
    INPUT_YOURSELF_NAME,
    INPUT_DATE_BIRTHSDAY,

    //----- update -----//

    UPDATE_TEXT,
    UPDATE_TITLE,
    UPDATE_GMT,
    UPDATE_YOURSELF_NAME,
    UPDATE_DATE_BIRTHSDAY,

    /// --- in progress ---///
    INPUT_PHOTO,
    INPUT_MESSAGE_TO_AI
}
