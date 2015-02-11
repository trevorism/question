package com.brooks.question

import redis.clients.jedis.Jedis

/**
 * Created by trevo_000 on 1/31/2015.
 */
class HelloWorldTest {

    public static void main(String [] args){
        getKey()
        //Jedis jedis = new Jedis("localhost");
        //jedis.connect();

        //jedis.ping();
        //jedis.close();
    }

    private static void getKey() {
        Jedis jedis = new Jedis("localhost")
        //jedis.connect();
        def get = jedis.get("first")
        println get
        jedis.close()
    }

    private static void setKey() {
        Jedis jedis = new Jedis("localhost")
        jedis.connect()
        jedis.set("first", "doope")
        jedis.close();

    }


}
