package JavaTask.controller;

import JavaTask.dto.StatisticsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/totalStatisticsByDate")
    public ResponseEntity<String> getTotalStatisticsByDate() {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    match(Criteria.where("reportSpecification.reportType").is("GET_SALES_AND_TRAFFIC_REPORT")),
                    Aggregation.unwind("salesAndTrafficByDate"),
                    group("salesAndTrafficByDate.date")
                            .sum("salesAndTrafficByDate.salesByDate.orderedProductSales.amount").as("totalAmount")
            );

            AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "task", Document.class);
            List<Document> totalStatisticsByDate = result.getMappedResults();

            if (!totalStatisticsByDate.isEmpty()) {
                // Використовуйте ObjectMapper для форматування результату в JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(totalStatisticsByDate);

                return ResponseEntity.ok(jsonResult);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No statistics found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }

    @GetMapping("/totalStatisticsByDat")
    public ResponseEntity<String> getTotalStatisticsByDate(@RequestParam("selectedDate") String selectedDate) {
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

            AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "task", Document.class);
            List<Document> selectedDateStatistics = result.getMappedResults();

            if (!selectedDateStatistics.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(selectedDateStatistics);
                return ResponseEntity.ok(jsonResult);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No statistics found for the selected date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }








}

