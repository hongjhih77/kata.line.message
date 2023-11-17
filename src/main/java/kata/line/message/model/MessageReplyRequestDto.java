package kata.line.message.model;

import jakarta.validation.constraints.NotNull;

public class MessageReplyRequestDto {

    @NotNull
    String messageId;
    @NotNull
    String text;
    boolean notificationDisabled;

    public String getMessageId() {
        return messageId;
    }

    public String getText() {
        return text;
    }

    public boolean isNotificationDisabled() {
        return notificationDisabled;
    }
}
