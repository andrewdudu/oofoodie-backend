package com.oofoodie.backend.models.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "locationss")
public class RestaurantLocation {

    @Id
    private String restoId;

    @GeoPointField
    private GeoPoint location;
}
