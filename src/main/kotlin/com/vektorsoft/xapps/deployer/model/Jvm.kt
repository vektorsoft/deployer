/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso(MavenDependency::class)
class Jvm(@XmlAttribute val version : String = "8") {

    @XmlTransient
    val dependenciesProperty = SimpleListProperty<JvmDependency>(FXCollections.observableArrayList())
    var dependencies : List<JvmDependency>
    @XmlElementWrapper(name = "dependencies") @XmlElement(name = "dependency") get() = dependenciesProperty.get()
    set(value)  {
        dependenciesProperty.clear()
        dependenciesProperty.addAll(value)
    }

    @XmlElement(name = "platform-specific-dependencies")
    val platformDependencies = PlatformSpecificDependencies()


    fun addDependency(dependency : JvmDependency) = dependenciesProperty.add(dependency)


}