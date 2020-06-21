package com.oofoodie.backend.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingStats {

    private Float five;

    private Float four;

    private Float three;

    private Float two;

    private Float one;

    private Float avgStar;
}
