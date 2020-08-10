package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.VoucherRepository;
import com.oofoodie.backend.validation.VoucherMustExist;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class VoucherMustExistValidator extends AbstractReactiveConstraintValidator<VoucherMustExist, String> {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public Mono<Boolean> validate(String value, VoucherMustExist annotation, ConstraintValidatorContext context) {
        return voucherRepository.findById(value)
                .map(voucher -> true)
                .switchIfEmpty(Mono.fromCallable(() -> false));
    }
}
