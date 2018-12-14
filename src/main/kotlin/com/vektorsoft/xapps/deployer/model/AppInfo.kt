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

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections

class AppInfo {

    @JsonIgnore
    val appNameProperty : StringProperty = SimpleStringProperty("")
    var name : String
        get() = appNameProperty.get()
        set(value) = appNameProperty.set(value)

    @JsonIgnore
    val descriptionProperty = SimpleStringProperty("")
    var description : String?
        @JacksonXmlCData get() = descriptionProperty.get()
        set(value) = descriptionProperty.set(value)

    @JsonIgnore
    val iconsProperty = SimpleListProperty<BinaryData>(FXCollections.observableArrayList())
    var icons : List<BinaryData>
    @JacksonXmlElementWrapper(localName = "icons")
    @JacksonXmlProperty(localName = "icon")
    get() = iconsProperty.get()
    set(value)  {
        iconsProperty.clear()
        iconsProperty.addAll(value)
    }

    fun addIcon(icon : BinaryData) {
        iconsProperty.add(icon)
    }
}