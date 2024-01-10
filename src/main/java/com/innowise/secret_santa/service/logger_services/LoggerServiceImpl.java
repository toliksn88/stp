package com.innowise.secret_santa.service.logger_services;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoggerServiceImpl<T> implements LoggerService<T> {

    private final Logger logger;

    public LoggerServiceImpl(Logger logger) {
        this.logger = logger;
    }

    public void loggerInfo(String message, Object... objects) {

        logger.info(message, objects);
    }

    @Override
    public T logger(String message, T objects) {
        logger.info(message, objects);
        return objects;
    }
}