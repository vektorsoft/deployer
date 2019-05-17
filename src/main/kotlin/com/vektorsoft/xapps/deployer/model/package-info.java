/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(value = StringAdapter.class, type = String.class)
        //@XmlJavaTypeAdapter(value = JdkVersionAdapter.class, type = JdkVersion.class)
})
package com.vektorsoft.xapps.deployer.model;

import com.vektorsoft.xapps.deployer.persist.xml.JdkVersionAdapter;
import com.vektorsoft.xapps.deployer.persist.xml.StringAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;