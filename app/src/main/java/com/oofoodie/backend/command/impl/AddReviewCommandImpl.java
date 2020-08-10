package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.AddReviewCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.Review;
import com.oofoodie.backend.models.entity.Timeline;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.ReviewRequest;
import com.oofoodie.backend.models.response.ReviewResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.repository.ReviewRepository;
import com.oofoodie.backend.repository.TimelineRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AddReviewCommandImpl implements AddReviewCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<ReviewResponse> execute(ReviewRequest request) {
        return restaurantRepository.findByIdAndStatus(request.getRestoId(), true)
                .flatMap(restaurant -> addReview(request, restaurant))
                .map(this::constructResponse)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Restaurant is not found"))));
    }

    private Mono<Review> addReview(ReviewRequest request, Restaurant restaurant) {
        Boolean hasReviewed = false;
        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(request, review);

        List<Review> reviews;
        if (restaurant.getReviews() == null) {
            reviews = new ArrayList<>();
        } else {
            reviews = restaurant.getReviews();
            for (Review rev : reviews) {
                if (rev.getUser().equals(request.getUser())) {
                    hasReviewed = true;

                    rev.setStar(review.getStar());
                    rev.setComment(review.getComment());

                    BeanUtils.copyProperties(rev, review);
                }
            }
        }

        if (!hasReviewed) reviews.add(review);
        restaurant.setReviews(reviews);

        return restaurantRepository.save(restaurant)
                .flatMap(res -> getRedisData.getUser(review.getUser()))
                .flatMap(user -> addTimeline(user, review, restaurant.getName()))
                .flatMap(res -> reviewRepository.save(review));
    }

    private Mono<User> addTimeline(User user, Review review, String restaurantName) {
        Boolean hasReviewed = false;
        Timeline timeline = Timeline.builder()
                .likes(new ArrayList<>())
                .type("review")
                .restaurantName(restaurantName)
                .username(review.getUser())
                .reviewId(review.getId())
                .build();
        timeline.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(review, timeline);

        List<Timeline> timelines;
        if (user.getTimelines() == null) {
            timelines = new ArrayList<>();
        } else {
            timelines = user.getTimelines();
            for (Timeline tl : timelines) {
                if (tl.getReviewId().equals(review.getId())) {
                    hasReviewed = true;

                    tl.setStar(review.getStar());
                    tl.setComment(review.getComment());

                    BeanUtils.copyProperties(tl, timeline);
                }
            }
        }

        if (!hasReviewed) {
            timeline.setNumber(timelines.size());
            timelines.add(timeline);
        }
        user.setTimelines(timelines);

        return timelineRepository.save(timeline)
                .flatMap(tl -> userRepository.save(user))
                .doOnNext(savedUser -> getRedisData.saveUser(savedUser));
    }

    private ReviewResponse constructResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        BeanUtils.copyProperties(review, reviewResponse);

        return reviewResponse;
    }
}
