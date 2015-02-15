package com.brooks.question.api

import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory

/**
 * @author tbrooks
 */
class MemoryBasedLucene implements Lucene {

    final Directory directory

    MemoryBasedLucene(){
        directory = new RAMDirectory()
    }

}
