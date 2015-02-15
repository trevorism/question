package com.brooks.question.api

import org.junit.Test

/**
 * @author tbrooks
 */
class IdGeneratorTest {

    @Test
    void testGetNextId() {
        IdGenerator idGenerator = IdGenerator.instance
        idGenerator.setId(0)
        assert 1 == idGenerator.nextId
        assert 2 == idGenerator.nextId
    }

    @Test
    void testGetNextIdWithSeed() {
        IdGenerator idGenerator = IdGenerator.instance
        idGenerator.setId(5)
        assert 6 == idGenerator.nextId
        assert 7 == idGenerator.nextId
    }
}
