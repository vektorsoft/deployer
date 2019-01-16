/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.persist.xml

import javax.xml.bind.annotation.adapters.XmlAdapter


class StringAdapter : XmlAdapter<String, String>() {

    override fun marshal(value : String?): String?  {
        return if(value == "") null else value
    }

    override fun unmarshal(value: String?): String {
        return value ?: "";
    }
}