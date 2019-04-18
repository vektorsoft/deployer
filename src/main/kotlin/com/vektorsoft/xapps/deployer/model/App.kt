/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
class App {


    @XmlTransient
    val versionProperty = SimpleStringProperty("")
    var version : String
        @XmlAttribute get() = versionProperty.get()
    set(value) = versionProperty.set(value)

    val info = AppInfo()
    val jvm = Jvm()

    @XmlTransient
    val appIdProperty = SimpleStringProperty("")
    @XmlAttribute(name = "application-id")
    fun getAppId() = appIdProperty.get()
    fun setAppId(value : String) = appIdProperty.set(value)


    @XmlTransient
    val serverProperty = SimpleObjectProperty<Server>()
    @XmlElement
    fun getServer() = serverProperty.get()
    fun setServer(value : Server) = serverProperty.set(value)

    @XmlTransient
    val argumentsProperty = SimpleObjectProperty<String>()
    var arguments : String?
        @XmlElement(name = "args") get() = argumentsProperty.get()
        set(value) = argumentsProperty.set(value)

}