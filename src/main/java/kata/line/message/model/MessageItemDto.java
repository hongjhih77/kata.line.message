package kata.line.message.model;

import com.linecorp.bot.webhook.model.MessageEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

public record MessageItemDto(String messageId, String userId, String text, long timestampMilli) {


    public static final class MessageItemDtoBuilder {
        private String messageId;
        private String userId;
        private String text;
        private long timestampMilli;

        private MessageItemDtoBuilder() {
        }

        public static MessageItemDtoBuilder aMessageItemDto() {
            return new MessageItemDtoBuilder();
        }

        public MessageItemDtoBuilder withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public MessageItemDtoBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public MessageItemDtoBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public MessageItemDtoBuilder withTimestampMilli(long timestampMilli) {
            this.timestampMilli = timestampMilli;
            return this;
        }

        public MessageItemDto build() {
            return new MessageItemDto(messageId, userId, text, timestampMilli);
        }
    }
}
