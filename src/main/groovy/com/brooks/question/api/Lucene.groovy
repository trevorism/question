package com.brooks.question.api

import org.apache.lucene.store.Directory

/**
 * @author tbrooks
 */
interface Lucene {

    Directory getDirectory()

}