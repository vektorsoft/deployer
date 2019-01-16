/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.maven

import org.apache.maven.shared.invoker.PrintStreamHandler

class MavenErrorHandler : PrintStreamHandler() {

    override fun consumeLine(line: String?) {
        println("error line: $line")
    }
}