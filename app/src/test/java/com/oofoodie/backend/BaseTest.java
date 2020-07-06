package com.oofoodie.backend;

import com.oofoodie.backend.models.elastic.RestaurantLocation;
import com.oofoodie.backend.models.entity.*;
import com.oofoodie.backend.models.request.Hour;
import com.oofoodie.backend.models.request.OpenHourRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseTest {

    protected Timeline constructTimeline() {
        return Timeline.builder()
                .reviewId("id")
                .star(5)
                .build();
    }

    protected User constructUser() {
        Timeline timeline = constructTimeline();
        timeline.setType("review");

        List<Timeline> timelines = new ArrayList(Collections.singleton(constructTimeline()));

        return User.builder()
                .username("user")
                .timelines(timelines)
                .build();
    }

    protected Review constructReview() {
        Review review = Review.builder()
                .restoId("")
                .user("user")
                .star(5)
                .build();
        review.setId("id");

        return review;
    }

    protected Location constructLocation() {
        return Location.builder()
                .lat((float) 3.0)
                .lon((float) 3.0)
                .build();
    }

    protected Hour constructHour() {
        return Hour.builder()
                .open("8:00")
                .close("23:00")
                .build();
    }

    protected OpenHourRequest constructOpenHourRequest() {
        return OpenHourRequest.builder()
                .friday(constructHour())
                .monday(constructHour())
                .saturday(constructHour())
                .sunday(constructHour())
                .thursday(constructHour())
                .tuesday(constructHour())
                .wednesday(constructHour())
                .build();
    }

    protected RestaurantLocation constructRestaurantLocation() {
        GeoPoint geoPoint = new GeoPoint((float) 3.0, (float) 3.0);
        return RestaurantLocation.builder()
                .location(geoPoint)
                .build();
    }

    protected Restaurant constructRestaurant() {
        return Restaurant.builder()
                .location(constructLocation())
                .openHour(constructOpenHourRequest())
                .image("success")
                .build();
    }
}
