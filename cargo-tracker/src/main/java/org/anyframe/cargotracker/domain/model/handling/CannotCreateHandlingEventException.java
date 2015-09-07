package org.anyframe.cargotracker.domain.model.handling;

import org.springframework.transaction.annotation.Transactional;

/**
 * If a {@link net.java.cargotracker.domain.model.handling.HandlingEvent} can't
 * be created from a given set of parameters.
 *
 * It is a checked exception because it's not a programming error, but rather a
 * special case that the application is built to handle. It can occur during
 * normal program execution.
 */
@Transactional(rollbackFor = CannotCreateHandlingEventException.class) //트랜잭션 롤백 여부를 지정
public class CannotCreateHandlingEventException extends Exception {

    private static final long serialVersionUID = 1L;

    public CannotCreateHandlingEventException(Exception e) {
        super(e);
    }

    public CannotCreateHandlingEventException() {
        super();
    }
}
