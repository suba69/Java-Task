package JavaTask.controller;

import JavaTask.serviceUser.CacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CacheService cacheService;


    @GetMapping("/statisticsBySpecifiedDate")
    @Cacheable(value = "statisticsByDateCache", key = "#selectedDate")
    public ResponseEntity<String> getStatisticsBySpecifiedDate(@RequestParam("selectedDate") String selectedDate) {

        String additionalParams = "selectedDate=" + selectedDate;

        String cacheKey = "statisticsBySpecifiedDate:" + additionalParams;
        Object cachedResult = cacheService.getFromCache(cacheKey);

        if (cachedResult != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cachedResult.toString());
        }

        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    match(Criteria.where("reportSpecification.reportType").is("GET_SALES_AND_TRAFFIC_REPORT")),
                    unwind("salesAndTrafficByDate"),
                    match(Criteria.where("salesAndTrafficByDate.date").is(selectedDate)),
                    project()
                            .and("salesAndTrafficByDate.date").as("date")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSales.amount").as("salesByDate.orderedProductSales.amount")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSales.currencyCode").as("salesByDate.orderedProductSales.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSalesB2B.amount").as("salesByDate.orderedProductSalesB2B.amount")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSalesB2B.currencyCode").as("salesByDate.orderedProductSalesB2B.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.unitsOrdered").as("salesByDate.unitsOrdered")
                            .and("salesAndTrafficByDate.salesByDate.unitsOrderedB2B").as("salesByDate.unitsOrderedB2B")
                            .and("salesAndTrafficByDate.salesByDate.totalOrderItems").as("salesByDate.totalOrderItems")
                            .and("salesAndTrafficByDate.salesByDate.totalOrderItemsB2B").as("salesByDate.totalOrderItemsB2B")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItem.amount").as("salesByDate.averageSalesPerOrderItem.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItem.currencyCode").as("salesByDate.averageSalesPerOrderItem.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItemB2B.amount").as("salesByDate.averageSalesPerOrderItemB2B.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItemB2B.currencyCode").as("salesByDate.averageSalesPerOrderItemB2B.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.averageUnitsPerOrderItem").as("salesByDate.averageUnitsPerOrderItem")
                            .and("salesAndTrafficByDate.salesByDate.averageUnitsPerOrderItemB2B").as("salesByDate.averageUnitsPerOrderItemB2B")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPrice.amount").as("salesByDate.averageSellingPrice.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPrice.currencyCode").as("salesByDate.averageSellingPrice.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPriceB2B.amount").as("salesByDate.averageSellingPriceB2B.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPriceB2B.currencyCode").as("salesByDate.averageSellingPriceB2B.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.unitsRefunded").as("salesByDate.unitsRefunded")
                            .and("salesAndTrafficByDate.salesByDate.refundRate").as("salesByDate.refundRate")
                            .and("salesAndTrafficByDate.salesByDate.claimsGranted").as("salesByDate.claimsGranted")
                            .and("salesAndTrafficByDate.salesByDate.claimsAmount.amount").as("salesByDate.claimsAmount.amount")
                            .and("salesAndTrafficByDate.salesByDate.claimsAmount.currencyCode").as("salesByDate.claimsAmount.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.shippedProductSales.amount").as("salesByDate.shippedProductSales.amount")
                            .and("salesAndTrafficByDate.salesByDate.shippedProductSales.currencyCode").as("salesByDate.shippedProductSales.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.unitsShipped").as("salesByDate.unitsShipped")
                            .and("salesAndTrafficByDate.trafficByDate.browserPageViews").as("trafficByDate.browserPageViews")
                            .and("salesAndTrafficByDate.trafficByDate.browserPageViewsB2B").as("trafficByDate.browserPageViewsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppPageViews").as("trafficByDate.mobileAppPageViews")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppPageViewsB2B").as("trafficByDate.mobileAppPageViewsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.pageViews").as("trafficByDate.pageViews")
                            .and("salesAndTrafficByDate.trafficByDate.pageViewsB2B").as("trafficByDate.pageViewsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.browserSessions").as("trafficByDate.browserSessions")
                            .and("salesAndTrafficByDate.trafficByDate.browserSessionsB2B").as("trafficByDate.browserSessionsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppSessions").as("trafficByDate.mobileAppSessions")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppSessionsB2B").as("trafficByDate.mobileAppSessionsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.sessions").as("trafficByDate.sessions")
                            .and("salesAndTrafficByDate.trafficByDate.sessionsB2B").as("trafficByDate.sessionsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.buyBoxPercentage").as("trafficByDate.buyBoxPercentage")
                            .and("salesAndTrafficByDate.trafficByDate.buyBoxPercentageB2B").as("trafficByDate.buyBoxPercentageB2B")
                            .and("salesAndTrafficByDate.trafficByDate.orderItemSessionPercentage").as("trafficByDate.orderItemSessionPercentage")
                            .and("salesAndTrafficByDate.trafficByDate.orderItemSessionPercentageB2B").as("trafficByDate.orderItemSessionPercentageB2B")
                            .and("salesAndTrafficByDate.trafficByDate.unitSessionPercentage").as("trafficByDate.unitSessionPercentage")
                            .and("salesAndTrafficByDate.trafficByDate.unitSessionPercentageB2B").as("trafficByDate.unitSessionPercentageB2B")
                            .and("salesAndTrafficByDate.trafficByDate.averageOfferCount").as("trafficByDate.averageOfferCount")
                            .and("salesAndTrafficByDate.trafficByDate.averageParentItems").as("trafficByDate.averageParentItems")
                            .and("salesAndTrafficByDate.trafficByDate.feedbackReceived").as("trafficByDate.feedbackReceived")
                            .and("salesAndTrafficByDate.trafficByDate.negativeFeedbackReceived").as("trafficByDate.negativeFeedbackReceived")
                            .and("salesAndTrafficByDate.trafficByDate.receivedNegativeFeedbackRate").as("trafficByDate.receivedNegativeFeedbackRate")
                            .andExclude("_id")

            );

            AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "task", Document.class);
            List<Document> resultDocuments = results.getMappedResults();

            Object resultObject = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(resultDocuments));

            cacheService.saveToCache(cacheKey, resultObject);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resultObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/totalStatisticsAllDate")
    public ResponseEntity<Object> getTotalStatistics() {

        String additionalParams = "";

        String cacheKey = "totalStatisticsAllDate:" + additionalParams;
        Object cachedResult = cacheService.getFromCache(cacheKey);

        if (cachedResult != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cachedResult);
        }


        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    match(Criteria.where("reportSpecification.reportType").is("GET_SALES_AND_TRAFFIC_REPORT")),
                    unwind("salesAndTrafficByDate"),
                    project()
                            .and("salesAndTrafficByDate.date").as("date")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSales.amount").as("salesByDate.orderedProductSales.amount")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSales.currencyCode").as("salesByDate.orderedProductSales.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSalesB2B.amount").as("salesByDate.orderedProductSalesB2B.amount")
                            .and("salesAndTrafficByDate.salesByDate.orderedProductSalesB2B.currencyCode").as("salesByDate.orderedProductSalesB2B.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.unitsOrdered").as("salesByDate.unitsOrdered")
                            .and("salesAndTrafficByDate.salesByDate.unitsOrderedB2B").as("salesByDate.unitsOrderedB2B")
                            .and("salesAndTrafficByDate.salesByDate.totalOrderItems").as("salesByDate.totalOrderItems")
                            .and("salesAndTrafficByDate.salesByDate.totalOrderItemsB2B").as("salesByDate.totalOrderItemsB2B")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItem.amount").as("salesByDate.averageSalesPerOrderItem.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItem.currencyCode").as("salesByDate.averageSalesPerOrderItem.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItemB2B.amount").as("salesByDate.averageSalesPerOrderItemB2B.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItemB2B.currencyCode").as("salesByDate.averageSalesPerOrderItemB2B.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.averageUnitsPerOrderItem").as("salesByDate.averageUnitsPerOrderItem")
                            .and("salesAndTrafficByDate.salesByDate.averageUnitsPerOrderItemB2B").as("salesByDate.averageUnitsPerOrderItemB2B")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPrice.amount").as("salesByDate.averageSellingPrice.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPrice.currencyCode").as("salesByDate.averageSellingPrice.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPriceB2B.amount").as("salesByDate.averageSellingPriceB2B.amount")
                            .and("salesAndTrafficByDate.salesByDate.averageSellingPriceB2B.currencyCode").as("salesByDate.averageSellingPriceB2B.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.unitsRefunded").as("salesByDate.unitsRefunded")
                            .and("salesAndTrafficByDate.salesByDate.refundRate").as("salesByDate.refundRate")
                            .and("salesAndTrafficByDate.salesByDate.claimsGranted").as("salesByDate.claimsGranted")
                            .and("salesAndTrafficByDate.salesByDate.claimsAmount.amount").as("salesByDate.claimsAmount.amount")
                            .and("salesAndTrafficByDate.salesByDate.claimsAmount.currencyCode").as("salesByDate.claimsAmount.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.shippedProductSales.amount").as("salesByDate.shippedProductSales.amount")
                            .and("salesAndTrafficByDate.salesByDate.shippedProductSales.currencyCode").as("salesByDate.shippedProductSales.currencyCode")
                            .and("salesAndTrafficByDate.salesByDate.unitsShipped").as("salesByDate.unitsShipped")
                            .and("salesAndTrafficByDate.trafficByDate.browserPageViews").as("trafficByDate.browserPageViews")
                            .and("salesAndTrafficByDate.trafficByDate.browserPageViewsB2B").as("trafficByDate.browserPageViewsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppPageViews").as("trafficByDate.mobileAppPageViews")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppPageViewsB2B").as("trafficByDate.mobileAppPageViewsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.pageViews").as("trafficByDate.pageViews")
                            .and("salesAndTrafficByDate.trafficByDate.pageViewsB2B").as("trafficByDate.pageViewsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.browserSessions").as("trafficByDate.browserSessions")
                            .and("salesAndTrafficByDate.trafficByDate.browserSessionsB2B").as("trafficByDate.browserSessionsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppSessions").as("trafficByDate.mobileAppSessions")
                            .and("salesAndTrafficByDate.trafficByDate.mobileAppSessionsB2B").as("trafficByDate.mobileAppSessionsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.sessions").as("trafficByDate.sessions")
                            .and("salesAndTrafficByDate.trafficByDate.sessionsB2B").as("trafficByDate.sessionsB2B")
                            .and("salesAndTrafficByDate.trafficByDate.buyBoxPercentage").as("trafficByDate.buyBoxPercentage")
                            .and("salesAndTrafficByDate.trafficByDate.buyBoxPercentageB2B").as("trafficByDate.buyBoxPercentageB2B")
                            .and("salesAndTrafficByDate.trafficByDate.orderItemSessionPercentage").as("trafficByDate.orderItemSessionPercentage")
                            .and("salesAndTrafficByDate.trafficByDate.orderItemSessionPercentageB2B").as("trafficByDate.orderItemSessionPercentageB2B")
                            .and("salesAndTrafficByDate.trafficByDate.unitSessionPercentage").as("trafficByDate.unitSessionPercentage")
                            .and("salesAndTrafficByDate.trafficByDate.unitSessionPercentageB2B").as("trafficByDate.unitSessionPercentageB2B")
                            .and("salesAndTrafficByDate.trafficByDate.averageOfferCount").as("trafficByDate.averageOfferCount")
                            .and("salesAndTrafficByDate.trafficByDate.averageParentItems").as("trafficByDate.averageParentItems")
                            .and("salesAndTrafficByDate.trafficByDate.feedbackReceived").as("trafficByDate.feedbackReceived")
                            .and("salesAndTrafficByDate.trafficByDate.negativeFeedbackReceived").as("trafficByDate.negativeFeedbackReceived")
                            .and("salesAndTrafficByDate.trafficByDate.receivedNegativeFeedbackRate").as("trafficByDate.receivedNegativeFeedbackRate")
                            .andExclude("_id")
            );

            AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "task", Document.class);
            List<Document> resultDocuments = results.getMappedResults();

            Object resultObject = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(resultDocuments));

            cacheService.saveToCache(cacheKey, resultObject);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resultObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/totalStatisticsAllASIN")
    public ResponseEntity<Object> getTotalStatisticsByASIN() {
        String additionalParams = "";

        String cacheKey = "totalStatisticsAllASIN:" + additionalParams;
        Object cachedResult = cacheService.getFromCache(cacheKey);

        if (cachedResult != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cachedResult);
        }

        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    match(Criteria.where("reportSpecification.reportType").is("GET_SALES_AND_TRAFFIC_REPORT")),
                    unwind("salesAndTrafficByAsin"),
                    project()
                            .and("salesAndTrafficByAsin.parentAsin").as("parentAsin")
                            .and("salesAndTrafficByAsin.salesByAsin.unitsOrdered").as("salesByAsin.unitsOrdered")
                            .and("salesAndTrafficByAsin.salesByAsin.unitsOrderedB2B").as("salesByAsin.unitsOrderedB2B")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSales.amount").as("salesByAsin.orderedProductSales.amount")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSales.currencyCode").as("salesByAsin.orderedProductSales.currencyCode")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSalesB2B.amount").as("salesByAsin.orderedProductSalesB2B.amount")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSalesB2B.currencyCode").as("salesByAsin.orderedProductSalesB2B.currencyCode")
                            .and("salesAndTrafficByAsin.salesByAsin.totalOrderItems").as("salesByAsin.totalOrderItems")
                            .and("salesAndTrafficByAsin.salesByAsin.totalOrderItemsB2B").as("salesByAsin.totalOrderItemsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessions").as("trafficByAsin.browserSessions")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessionsB2B").as("trafficByAsin.browserSessionsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessions").as("trafficByAsin.mobileAppSessions")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionsB2B").as("trafficByAsin.mobileAppSessionsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessions").as("trafficByAsin.sessions")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessionsB2B").as("trafficByAsin.sessionsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessionPercentage").as("trafficByAsin.browserSessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessionPercentageB2B").as("trafficByAsin.browserSessionPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionPercentage").as("trafficByAsin.mobileAppSessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionPercentageB2B").as("trafficByAsin.mobileAppSessionPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessionPercentage").as("trafficByAsin.sessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessionPercentageB2B").as("trafficByAsin.sessionPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViews").as("trafficByAsin.browserPageViews")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViewsB2B").as("trafficByAsin.browserPageViewsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViews").as("trafficByAsin.mobileAppPageViews")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsB2B").as("trafficByAsin.mobileAppPageViewsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViews").as("trafficByAsin.pageViews")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsB2B").as("trafficByAsin.pageViewsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViewsPercentage").as("trafficByAsin.browserPageViewsPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViewsPercentageB2B").as("trafficByAsin.browserPageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsPercentage").as("trafficByAsin.mobileAppPageViewsPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsPercentageB2B").as("trafficByAsin.mobileAppPageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentageB2B").as("trafficByAsin.pageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentage").as("trafficByAsin.pageViewsPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentageB2B").as("trafficByAsin.pageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.buyBoxPercentage").as("trafficByAsin.buyBoxPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.buyBoxPercentageB2B").as("trafficByAsin.buyBoxPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.unitSessionPercentage").as("trafficByAsin.unitSessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.unitSessionPercentageB2B").as("trafficByAsin.unitSessionPercentageB2B")
                            .andExclude("_id")
            );

            AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "task", Document.class);
            List<Document> resultDocuments = results.getMappedResults();

            Object resultObject = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(resultDocuments));

            cacheService.saveToCache(cacheKey, resultObject);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resultObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/totalStatisticsByASIN")
    public ResponseEntity<Object> getTotalStatisticsByASIN(@RequestParam String asin) {

        String additionalParams = "asin=" + asin;
        String cacheKey = "totalStatisticsByASIN:" + additionalParams;
        Object cachedResult = cacheService.getFromCache(cacheKey);

        if (cachedResult != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cachedResult);
        }

        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    match(Criteria.where("reportSpecification.reportType").is("GET_SALES_AND_TRAFFIC_REPORT")),
                    unwind("salesAndTrafficByAsin"),
                    project()
                            .and("salesAndTrafficByAsin.parentAsin").as("parentAsin")
                            .and("salesAndTrafficByAsin.salesByAsin.unitsOrdered").as("salesByAsin.unitsOrdered")
                            .and("salesAndTrafficByAsin.salesByAsin.unitsOrderedB2B").as("salesByAsin.unitsOrderedB2B")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSales.amount").as("salesByAsin.orderedProductSales.amount")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSales.currencyCode").as("salesByAsin.orderedProductSales.currencyCode")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSalesB2B.amount").as("salesByAsin.orderedProductSalesB2B.amount")
                            .and("salesAndTrafficByAsin.salesByAsin.orderedProductSalesB2B.currencyCode").as("salesByAsin.orderedProductSalesB2B.currencyCode")
                            .and("salesAndTrafficByAsin.salesByAsin.totalOrderItems").as("salesByAsin.totalOrderItems")
                            .and("salesAndTrafficByAsin.salesByAsin.totalOrderItemsB2B").as("salesByAsin.totalOrderItemsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessions").as("trafficByAsin.browserSessions")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessionsB2B").as("trafficByAsin.browserSessionsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessions").as("trafficByAsin.mobileAppSessions")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionsB2B").as("trafficByAsin.mobileAppSessionsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessions").as("trafficByAsin.sessions")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessionsB2B").as("trafficByAsin.sessionsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessionPercentage").as("trafficByAsin.browserSessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserSessionPercentageB2B").as("trafficByAsin.browserSessionPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionPercentage").as("trafficByAsin.mobileAppSessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionPercentageB2B").as("trafficByAsin.mobileAppSessionPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessionPercentage").as("trafficByAsin.sessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.sessionPercentageB2B").as("trafficByAsin.sessionPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViews").as("trafficByAsin.browserPageViews")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViewsB2B").as("trafficByAsin.browserPageViewsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViews").as("trafficByAsin.mobileAppPageViews")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsB2B").as("trafficByAsin.mobileAppPageViewsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViews").as("trafficByAsin.pageViews")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsB2B").as("trafficByAsin.pageViewsB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViewsPercentage").as("trafficByAsin.browserPageViewsPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.browserPageViewsPercentageB2B").as("trafficByAsin.browserPageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsPercentage").as("trafficByAsin.mobileAppPageViewsPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsPercentageB2B").as("trafficByAsin.mobileAppPageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentageB2B").as("trafficByAsin.pageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentage").as("trafficByAsin.pageViewsPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentageB2B").as("trafficByAsin.pageViewsPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.buyBoxPercentage").as("trafficByAsin.buyBoxPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.buyBoxPercentageB2B").as("trafficByAsin.buyBoxPercentageB2B")
                            .and("salesAndTrafficByAsin.trafficByAsin.unitSessionPercentage").as("trafficByAsin.unitSessionPercentage")
                            .and("salesAndTrafficByAsin.trafficByAsin.unitSessionPercentageB2B").as("trafficByAsin.unitSessionPercentageB2B")
                            .andExclude("_id")
            );

            AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "task", Document.class);
            List<Document> resultDocuments = results.getMappedResults();

            Object resultObject = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(resultDocuments));

            cacheService.saveToCache(cacheKey, resultObject);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resultObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
