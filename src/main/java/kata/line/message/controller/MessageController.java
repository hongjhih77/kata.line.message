package kata.line.message.controller;

import com.linecorp.bot.parser.LineSignatureValidator;
import com.linecorp.bot.parser.WebhookParseException;
import com.linecorp.bot.parser.WebhookParser;
import com.linecorp.bot.webhook.model.MessageEvent;
import jakarta.annotation.Resource;
import kata.line.message.dao.MessageEventRepository;
import kata.line.message.mapper.MessageEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Resource
    private MessageEventRepository messageEventRepository;
    private final WebhookParser webhookParser;

    public MessageController(@Value("${line.bot.channel-secret}") String channelSecret) {
        this.webhookParser = new WebhookParser(new LineSignatureValidator(channelSecret.getBytes()));
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

}
