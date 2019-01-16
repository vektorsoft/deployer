/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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