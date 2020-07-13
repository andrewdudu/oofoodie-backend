package com.oofoodie.backend.helper;

import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.Review;
import com.oofoodie.backend.models.response.RatingStats;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculateRatingStatsHelper {

    public RatingStats calculateRatingStats(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();

        RatingStats ratingStats = RatingStats.builder()
                .five((float) 0)
                .four((float) 0)
                .three((float) 0)
                .two((float) 0)
                .one((float) 0)
                .avgStar((float) 0)
                .build();

        for (Review review : reviews) {
            switch (review.getStar()) {
                case 5:
                    ratingStats.setFive(ratingStats.getFive() + 1);
                    break;
                case 4:
                    ratingStats.setFour(ratingStats.getFour() + 1);
                    break;
                case 3:
                    ratingStats.setThree(ratingStats.getThree() + 1);
                    break;
                case 2:
                    ratingStats.setTwo(ratingStats.getTwo() + 1);
                    break;
                case 1:
                    ratingStats.setOne(ratingStats.getOne() + 1);
                    break;
            }
            ratingStats.setAvgStar(ratingStats.getAvgStar() + review.getStar());
        }

        ratingStats.setAvgStar(ratingStats.getAvgStar() / reviews.size());
        ratingStats.setFive(ratingStats.getFive() / reviews.size() * 100);
        ratingStats.setFour(ratingStats.getFour() / reviews.size() * 100);
        ratingStats.setThree(ratingStats.getThree() / reviews.size() * 100);
        ratingStats.setTwo(ratingStats.getTwo() / reviews.size() * 100);
        ratingStats.setOne(ratingStats.getOne() / reviews.size() * 100);

        return ratingStats;
    }
}
