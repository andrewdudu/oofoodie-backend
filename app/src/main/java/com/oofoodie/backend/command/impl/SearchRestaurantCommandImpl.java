package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.SearchRestaurantCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.Review;
import com.oofoodie.backend.models.response.RatingStats;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantCustomRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class SearchRestaurantCommandImpl implements SearchRestaurantCommand {

    @Autowired
    private RestaurantCustomRepository restaurantRepository;

    @Override
    public Mono<List<RestaurantResponse>> execute(String term) {
        return restaurantRepository.search(term)
                .map(this::constructResponse)
                .collectList();
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        if (!Objects.isNull(restaurant.getReviews())) response.setRatingStats(calculateRatingStats(restaurant));
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }

    private RatingStats calculateRatingStats(Restaurant restaurant) {
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
