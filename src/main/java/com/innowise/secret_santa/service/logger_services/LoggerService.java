package com.innowise.secret_santa.service.logger_services;

public interface LoggerService<T> {

    void loggerInfo(String message, Object ... objects);

    T logger(String message, T objects);


}
