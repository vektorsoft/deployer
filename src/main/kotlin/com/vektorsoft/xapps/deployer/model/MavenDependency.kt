/*
 * Copyright (c) 2018. Vladimir Djurovic
 *
 * This file is part of Deployer.
 *
 * Deployer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deployer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Deployer.  If not, see <https://www.gnu.org/licenses/>.
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