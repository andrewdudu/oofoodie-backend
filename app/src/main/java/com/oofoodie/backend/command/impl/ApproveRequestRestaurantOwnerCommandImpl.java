package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.ApproveRequestRestaurantOwnerCommand;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.command.ApproveRequestRestaurantOwnerCommandRequest;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApproveRequestRestaurantOwnerCommandImpl implements ApproveRequestRestaurantOwnerCommand {

    @Autowired
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<Boolean> execute(ApproveRequestRestaurantOwnerCommandRequest request) {
        return restaurantOwnerRequestRepository.findById(request.getRequestId())
                .doOnNext(restaurantOwnerRequest -> deleteRestaurantOwnerRequest(restaurantOwnerRequest.getId()))
                .flatMap(restaurantOwnerRequestRepository -> findUser(restaurantOwnerRequestRepository))
                .map(user -> true);
    }

    private Mono<User> findUser(RestaurantOwnerRequest restaurantOwnerRequest) {
        return userRepository.findByUsername(restaurantOwnerRequest.getMerchantUsername())
                .flatMap(user -> setRestaurantOwner(user, restaurantOwnerRequest.getRestaurantId()));
    }

    private Mono<User> setRestaurantOwner(User user, String restaurantId) {
        user.setRestaurantOwner(restaurantId);

        return userRepository.save(user)
                .doOnNext(savedUser -> getRedisData.saveUser(user));
    }

    private void deleteRestaurantOwnerRequest(String requestId) {
        restaurantOwnerRequestRepository.deleteById(requestId)
                .subscribe();
    }
}
