package com.brooks.question

import org.junit.Test

/**
 * Created by trevo_000 on 1/31/2015.
 */
class HelloWorldTest {

    @Test
    public void test(){
        assert "hello" == new HelloWorld().hello();
    }
}
