/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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