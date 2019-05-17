/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.persist.xml

import com.vektorsoft.xapps.deployer.model.JDK_11_VALUE
import com.vektorsoft.xapps.deployer.model.JdkVersion
import javax.xml.bind.annotation.adapters.XmlAdapter

class JdkVersionAdapter : XmlAdapter<JdkVersion, String>() {

    override fun marshal(displayValue : String?): JdkVersion {
        return JdkVersion.values().find { it.display == displayValue } ?: JdkVersion.JDK_11
    }

    override fun unmarshal(version: JdkVersion?): String {
        return version?.display ?: JDK_11_VALUE
    }
}