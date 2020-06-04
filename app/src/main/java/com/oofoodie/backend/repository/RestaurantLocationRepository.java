package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.elastic.RestaurantLocation;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantLocationRepository extends ElasticsearchRepository<RestaurantLocation, String> {

    List<RestaurantLocation> search(QueryBuilder query);
}
