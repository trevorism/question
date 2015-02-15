package com.brooks.question.api

import java.util.concurrent.atomic.AtomicLong

/**
 * @author tbrooks
 */
@Singleton
class IdGenerator {

    private AtomicLong id = new AtomicLong();

    public long getNextId(){
        id.incrementAndGet()
    }

    public void setId(long value){
        id = new AtomicLong(value)
    }
}
