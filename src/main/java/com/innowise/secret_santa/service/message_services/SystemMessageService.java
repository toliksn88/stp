package com.innowise.secret_santa.service.message_services;

import com.innowise.secret_santa.model.TypeMessage;

public interface SystemMessageService {

    void messageService(TypeMessage typeMessage, Long accountId, String ... emails);

}
