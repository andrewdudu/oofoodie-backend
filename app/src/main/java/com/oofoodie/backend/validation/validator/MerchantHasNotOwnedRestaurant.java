package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.validation.MerchantMustNotHasOwnedRestaurant;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MerchantHasNotOwnedRestaurant extends AbstractReactiveConstraintValidator<MerchantMustNotHasOwnedRestaurant, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<Boolean> validate(String value, MerchantMustNotHasOwnedRestaurant annotation, ConstraintValidatorContext context) {
        return userRepository.findByUsername(value)
                .map(this::checkIfHasOwned);

    }

    private boolean checkIfHasOwned(User user) {
        return Objects.isNull(user.getRestaurantOwner());
    }
}
