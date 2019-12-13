package com.btc.bootcamp.ludo.business;

import org.springframework.stereotype.Service;

@Service
public class IdentitiyGenerationService {

    private long id;

    public long generateId() {
        return ++id;
    }
}
