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

    fun addDependency(dependency : JvmDependency) = dependenciesProperty.add(dependency)
}