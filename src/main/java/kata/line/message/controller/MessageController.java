package kata.line.message.controller;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.ReplyMessageResponse;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.parser.LineSignatureValidator;
import com.linecorp.bot.parser.WebhookParseException;
import com.linecorp.bot.parser.WebhookParser;
import com.linecorp.bot.webhook.model.MessageEvent;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import kata.line.message.dao.MessageEventRepository;
import kata.line.message.dao.entity.MessageEventEntity;
import kata.line.message.mapper.MessageEventMapper;
import kata.line.message.model.MessageItemDto;
import kata.line.message.model.MessageReplyRequestDto;
import kata.line.message.model.MessageReplyResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Resource
    private MessageEventRepository messageEventRepository;
    private final WebhookParser webhookParser;

    private final MessagingApiClient messagingApiClient;

    public MessageController(@Value("${line.bot.channel-secret}") String channelSecret, MessagingApiClient messagingApiClient) {
        this.webhookParser = new WebhookParser(new LineSignatureValidator(channelSecret.getBytes()));
        this.messagingApiClient = messagingApiClient;
    }

    @RequestMapping("/webhook")
    public void webhook(@RequestBody String requestBody, @RequestHeader("x-line-signature") String xLineSignature) {
        try {
            var callbackRequest = webhookParser.handle(xLineSignature, requestBody.getBytes());
            for (var event : callbackRequest.events()) {
                if (event instanceof MessageEvent) {
                    messageEventRepository.insert(MessageEventMapper.toEntity((MessageEvent) event));
                }
            }
        } catch (IOException | WebhookParseException e) {
            logger.error("Failed to parse webhook request.", e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/messages/reply", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MessageReplyResultDto> replyMessage(@Valid @RequestBody MessageReplyRequestDto requestBody) {
        String messageId = requestBody.getMessageId();
        String textToSend = requestBody.getText();
        boolean notificationDisabled = requestBody.isNotificationDisabled();
        MessageEventEntity entity = messageEventRepository.findByMessageId(messageId);
        var responseBuilder = new MessageReplyResultDto.MessageReplyResultDtoBuilder();
        if (entity == null) {
            var errResponse = responseBuilder.withErrorMsg(String.format("messageId : %s not found", messageId)).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
        }

        CompletableFuture<Result<ReplyMessageResponse>> future = messagingApiClient.replyMessage(new ReplyMessageRequest(
                entity.getReplyToken(),
                List.of(new TextMessage(textToSend)),
                notificationDisabled));
        var sentMessageId = "";
        var errMsg = "";
        try {
            ReplyMessageResponse response = future.join().body();
            sentMessageId = response.sentMessages().get(0).id();
        } catch (RuntimeException e) {
            logger.info("Got an exception: " + e.getMessage(), e);
            if (e.getMessage().contains("Invalid reply token")) {
                errMsg = "Message Replied.";
            }
        }

        var messageReplyResultDto = responseBuilder
                .withSentMessageId(sentMessageId)
                .withErrorMsg(errMsg).build();
        return ResponseEntity.status(HttpStatus.OK).body(messageReplyResultDto);
    }

    @GetMapping("/messages")
    public List<MessageItemDto> getMessages(@RequestParam Optional<String> userId) {
        List<MessageEventEntity> entityList;
        if (userId.isEmpty()) {
            entityList = messageEventRepository.findAll();
        } else {
            entityList = messageEventRepository.findByUser(userId.get());
        }
        return MessageEventMapper.toMessageItemDtoList(entityList);
    }
}
