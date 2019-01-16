/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.model

import java.util.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute

//@XmlAccessorType(XmlAccessType.FIELD)
class MavenDependency(@XmlAttribute var groupId : String = "",
                      @XmlAttribute var artifactId : String = "",
                      @XmlAttribute var version : String = "",
                      @XmlAttribute var packaging : String = "jar",
                      @XmlAttribute var classifier : String? = null, name : String = "", fileHash : String = "", fileSize : Long = 0, scope: JvmDependencyScope = JvmDependencyScope.CLASSPATH)
    : JvmDependency(name, "", fileHash, fileSize, scope) {

    override fun equals(other: Any?): Boolean {
        if(other is MavenDependency) {
            return (other.groupId == groupId && other.artifactId == artifactId && other.version == version && other.packaging == packaging && other.classifier == classifier)
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(groupId, artifactId, version, packaging, classifier)
    }

}