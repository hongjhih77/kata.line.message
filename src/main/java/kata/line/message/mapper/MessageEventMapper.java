package kata.line.message.mapper;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import kata.line.message.dao.entity.MessageEventEntity;
import kata.line.message.model.MessageItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class MessageEventMapper {
    public static MessageEventEntity toEntity(MessageEvent source) {
        MessageEventEntity target = new MessageEventEntity();
        target.setUserId(source.source().userId());
        target.setReplyToken(source.replyToken());
        target.setMessageId(source.message().id());
        target.setText(((TextMessageContent) source.message()).text());
        target.setTimestampMilli(source.timestamp());
        return target;
    }

    public static MessageItemDto toMessageItemDto(MessageEventEntity source) {
        var builder = MessageItemDto.MessageItemDtoBuilder.aMessageItemDto();
        return builder.withMessageId(source.getMessageId())
                .withUserId(source.getUserId())
                .withText(source.getText())
                .withTimestampMilli(source.getTimestampMilli())
                .build();
    }

    public static List<MessageItemDto> toMessageItemDtoList(List<MessageEventEntity> sourceList) {
        return sourceList.stream().map(MessageEventMapper::toMessageItemDto).collect(Collectors.toList());
    }
}
