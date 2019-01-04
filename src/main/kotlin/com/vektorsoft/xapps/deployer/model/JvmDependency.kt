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

import javafx.beans.property.SimpleObjectProperty
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlTransient

@XmlAccessorType(XmlAccessType.FIELD)
open class JvmDependency : BinaryData  {

    @XmlTransient
    val scopeProperty = SimpleObjectProperty<JvmDependencyScope>()

    var scope : JvmDependencyScope
    @XmlAttribute get() = scopeProperty.get()
    set(value) = scopeProperty.set(value)

    constructor(fileName : String, path : String, hash : String, size : Long, scope : JvmDependencyScope)  : super(fileName = fileName, path = path, hash = hash, size = size ) {
        this.scope = scope
    }
}