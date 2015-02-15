package com.brooks.question.api

import org.apache.lucene.store.Directory
import org.apache.lucene.store.SimpleFSDirectory

/**
 * @author tbrooks
 */

class FileBasedLucene implements Lucene {

    final Directory directory

    FileBasedLucene(String directoryName){
        File file = new File(directoryName)
        directory = new SimpleFSDirectory(file)
    }

}
