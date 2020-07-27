package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.AddRestaurantCommand;
import com.oofoodie.backend.models.elastic.RestaurantLocation;
import com.oofoodie.backend.models.entity.Location;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.AddRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantLocationRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ElasticsearchException;
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

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public Mono<RestaurantResponse> execute(AddRestaurantCommandRequest request) {
        String id = UUID.randomUUID().toString();
        return saveElasticLocation(request.getLocation(), id)
                .flatMap(restaurantLocation -> storeImg(request.getImage()))
                .map(img -> structureRequest(request, id, img))
                .flatMap(restaurant -> restaurantRepository.save(restaurant))
                .map(restaurant -> structureResponse(restaurant))
                .switchIfEmpty(Mono.error(new ElasticsearchException("Fail to save")));
    }

    private Mono<RestaurantLocation> saveElasticLocation(Location location, String id) {
        if (location != null && location.getLat() != null && location.getLon() != null) {
            GeoPoint geoPoint = new GeoPoint(location.getLat(), location.getLon());
            RestaurantLocation restoLocation = new RestaurantLocation();
            restoLocation.setLocation(geoPoint);
            restoLocation.setRestoId(id);

            return Mono.just(restaurantLocationRepository.save(restoLocation));
        }
        return Mono.empty();
    }

    private RestaurantResponse structureResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }

    private Restaurant structureRequest(AddRestaurantCommandRequest request, String id, String img) {
        Restaurant resto = new Restaurant();
        BeanUtils.copyProperties(request, resto);
        resto.setId(id);
        resto.setImage(img);

        return resto;
    }

    private Mono<String> storeImg(String img) {
        return commandExecutor.execute(AddImageCommandImpl.class, img)
                .subscribeOn(Schedulers.elastic());
    }
}
