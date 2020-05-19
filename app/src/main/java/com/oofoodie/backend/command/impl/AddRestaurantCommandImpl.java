package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.AddRestaurantCommand;
import com.oofoodie.backend.models.elastic.RestaurantLocation;
import com.oofoodie.backend.models.entity.Location;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.RestaurantRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantLocationRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class AddRestaurantCommandImpl implements AddRestaurantCommand {

    @Autowired
    private RestaurantLocationRepository restaurantLocationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<RestaurantResponse> execute(RestaurantRequest request) {
        String id = UUID.randomUUID().toString();
        Mono.fromCallable(() -> saveElasticLocation(request.getLocation(), id))
                .subscribeOn(Schedulers.elastic())
                .subscribe();
        return Mono.fromCallable(() -> structureRequest(request, id))
                .flatMap(restaurant -> restaurantRepository.save(restaurant))
                .map(restaurant -> structureResponse(restaurant));
    }

    private RestaurantLocation saveElasticLocation(Location location, String id) {
        if (location != null && location.getLat() != null && location.getLon() != null) {
            GeoPoint geoPoint = new GeoPoint(location.getLat(), location.getLon());
            RestaurantLocation restoLocation = new RestaurantLocation();
            restoLocation.setLocation(geoPoint);
            restoLocation.setRestoId(id);

            return restaurantLocationRepository.save(restoLocation);
        }
        return new RestaurantLocation();
    }

    private RestaurantResponse structureResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }

    private Restaurant structureRequest(RestaurantRequest request, String id) {
        Restaurant resto = new Restaurant();
        BeanUtils.copyProperties(request, resto);
        resto.setId(id);

        return resto;
    }
}
