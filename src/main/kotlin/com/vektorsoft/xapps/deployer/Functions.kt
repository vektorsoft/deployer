/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.security.MessageDigest

const val HASH_ALGORITHM = "SHA-1"
const val BUFFER_SIZE = 8192

fun filePathRelative(filePath : String, parent : String) : String {
    if(filePath.startsWith(parent)) {
        return filePath.substring(parent.length + 1)
    }
    return filePath
}

fun getLocalMavenRepoDir() : File {
    val homeDir = System.getProperty("user.home")
    return Path.of(homeDir, ".m2", "repository").toFile()
}


fun calculateFileHash(file : File) : String? {
    try {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        FileInputStream(file).use {
            val bytes = ByteArray(BUFFER_SIZE)
            var read : Int = it.read(bytes)
            while(read != -1) {
                digest.update(bytes, 0 , read)
                read = it.read(bytes)
            }
            val hashBytes = digest.digest()
            return toHex(hashBytes)
        }
    } catch( ex : Exception) {
        ex.printStackTrace()
    }
    return null
}

fun toHex(bytes : ByteArray) : String {
    val hexString  = StringBuilder()
    for (aMessageDigest:Byte in bytes) {
        var h: String = Integer.toHexString(0xFF and aMessageDigest.toInt())
        while (h.length < 2)
            h = "0$h"
        hexString.append(h)
    }
    return hexString.toString()
}

fun <T : Any> T.logger(clazz : Class<T>) : Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(clazz) }
}