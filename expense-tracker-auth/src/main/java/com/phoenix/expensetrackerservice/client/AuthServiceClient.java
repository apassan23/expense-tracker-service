package com.phoenix.expensetrackerservice.client;

import org.springframework.http.RequestEntity;

import java.io.Serializable;

public interface AuthServiceClient<Req extends RequestEntity<Void>, Res extends Serializable> {
    Res sendAndReceive(Req request);
}
