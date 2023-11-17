package kata.line.message.dao;

import kata.line.message.dao.entity.MessageEventEntity;

import java.util.List;

public interface MessageEventRepository {

    void insert(MessageEventEntity messageEvent);

    List<MessageEventEntity> findByUser(String userId);

    List<MessageEventEntity> findAll();

    MessageEventEntity findByMessageId(String messageId);
}
