package org.example.corepayuserservice.user.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.corepayuserservice.user.infrastructure.kafka.UserEventProducer;
import org.example.corepayuserservice.user.infrastructure.kafka.event.UserCreatedEvent;
import org.example.corepayuserservice.user.infrastructure.kafka.event.UserUpdatePasswordEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final UserEventProducer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void userCreatedEvent(UserCreatedEvent event){
        producer.sendUserCreated(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void userUpdatePasswordEvent(UserUpdatePasswordEvent event){
        producer.sendUserUpdatePassword(event);
    }


}
