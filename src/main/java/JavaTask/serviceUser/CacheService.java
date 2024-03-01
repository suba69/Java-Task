package JavaTask.serviceUser;

import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveToCache(String key, Object value) {
        BasicDBObject document = new BasicDBObject();
        document.append("_id", key)
                .append("data", value);
        mongoTemplate.save(document, "cache");
    }

    public Object getFromCache(String key) {
        Query query = new Query(Criteria.where("_id").is(key));
        BasicDBObject document = mongoTemplate.findOne(query, BasicDBObject.class, "cache");
        if (document != null) {
            return document.get("data");
        }
        return null;
    }
}
