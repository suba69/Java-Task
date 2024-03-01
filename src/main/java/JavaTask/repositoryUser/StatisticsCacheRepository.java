package JavaTask.repositoryUser;

import JavaTask.entity.StatisticsCache;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsCacheRepository extends MongoRepository<StatisticsCache, String> {
    StatisticsCache findBySelectedDate(String selectedDate);
    @Override
    <S extends StatisticsCache> S save(S entity);
}