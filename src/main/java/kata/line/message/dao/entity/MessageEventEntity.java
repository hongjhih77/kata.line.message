package kata.line.message.dao.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("MessageEvents")
public class MessageEventEntity {
    @Id
    String messageId;
    @Indexed
    String userId;
    String replyToken;
    String text;
    long timestampMilli;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyToken() {
        return replyToken;
    }

    public void setReplyToken(String replyToken) {
        this.replyToken = replyToken;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestampMilli() {
        return timestampMilli;
    }

    public void setTimestampMilli(long timestampMilli) {
        this.timestampMilli = timestampMilli;
    }
}
