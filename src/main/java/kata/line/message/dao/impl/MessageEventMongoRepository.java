package kata.line.message.dao.impl;

import jakarta.annotation.Resource;
import kata.line.message.dao.MessageEventRepository;
import kata.line.message.dao.entity.MessageEventEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageEventMongoRepository implements MessageEventRepository {

    @Resource
    MongoTemplate mongoTemplate;

    @Override
    public void insert(MessageEventEntity messageEvent) {
        mongoTemplate.insert(messageEvent);
    }

    @Override
    public List<MessageEventEntity> findByUser(String userId) {
        Criteria dateCriteria = Criteria.where("userId")
                .is(userId);
        Query query = new Query(dateCriteria);
        return mongoTemplate.find(query, MessageEventEntity.class);
    }

    @Override
    public List<MessageEventEntity> findAll() {
        return mongoTemplate.findAll(MessageEventEntity.class);
    }

    @Override
    public MessageEventEntity findByMessageId(String messageId) {
        return mongoTemplate.findById(messageId, MessageEventEntity.class);
    }
}
