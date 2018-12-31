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

import com.sun.xml.txw2.annotation.XmlCDATA
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.PROPERTY)
class AppInfo {

    @XmlTransient
    val appNameProperty : StringProperty = SimpleStringProperty("")
    var name : String
        get() = appNameProperty.get()
        set(value) = appNameProperty.set(value)

    @XmlTransient
    val descriptionProperty = SimpleStringProperty("")
    var description : String?
        @XmlCDATA get() = descriptionProperty.get()
        set(value) = descriptionProperty.set(value)

    @XmlTransient
    val iconsProperty = SimpleListProperty<BinaryData>(FXCollections.observableArrayList())
    var icons : List<BinaryData>
    @XmlElementWrapper(name = "icons")
    @XmlElement(name = "icon")
    get() = iconsProperty.get()
    set(value)  {
        iconsProperty.clear()
        iconsProperty.addAll(value)
    }

    fun addIcon(icon : BinaryData) {
        iconsProperty.add(icon)
    }

    fun removeIcon(path : String) {
        val toRemove = iconsProperty.find { it.path == path }
        iconsProperty.remove(toRemove)
    }
}