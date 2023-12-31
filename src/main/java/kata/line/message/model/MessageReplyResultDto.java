package kata.line.message.model;

public class MessageReplyResultDto {
    String sentMessageId;
    String errorMsg;

    public static final class MessageReplyResultDtoBuilder {
        private String sentMessageId;
        private String errorMsg;

        public MessageReplyResultDtoBuilder() {
        }

        public static MessageReplyResultDtoBuilder aMessageReplyResultDto() {
            return new MessageReplyResultDtoBuilder();
        }

        public MessageReplyResultDtoBuilder withSentMessageId(String sentMessageId) {
            this.sentMessageId = sentMessageId;
            return this;
        }

        public MessageReplyResultDtoBuilder withErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public MessageReplyResultDto build() {
            MessageReplyResultDto messageReplyResultDto = new MessageReplyResultDto();
            messageReplyResultDto.sentMessageId = this.sentMessageId;
            messageReplyResultDto.errorMsg = this.errorMsg;
            return messageReplyResultDto;
        }
    }

    public String getSentMessageId() {
        return sentMessageId;
    }

    public void setSentMessageId(String sentMessageId) {
        this.sentMessageId = sentMessageId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
