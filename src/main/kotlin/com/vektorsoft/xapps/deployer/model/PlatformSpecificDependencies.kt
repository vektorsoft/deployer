/*
 * Copyright (c) 2019. Vladimir Djurovic
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

@XmlAccessorType(XmlAccessType.FIELD)
class PlatformSpecificDependencies {

    @XmlTransient
    val macDependenciesProperty = SimpleListProperty<JvmDependency>(FXCollections.observableArrayList())
    @XmlElementWrapper(name = "mac")
    @XmlElement(name = "dependency")
    fun getMacDependencies() : List<JvmDependency> {
        return macDependenciesProperty.get()
    }

    fun setMacDependencies(value : List<JvmDependency>) {
        macDependenciesProperty.clear()
        macDependenciesProperty.addAll()
    }


    @XmlTransient
    val winDependenciesProperty = SimpleListProperty<JvmDependency>(FXCollections.observableArrayList())
    @XmlElementWrapper(name = "windows")
    @XmlElement(name = "dependency")
    fun getWindowsDependencies() = winDependenciesProperty.get()

    fun setWindowsDependencies(value : List<JvmDependency>) {
        winDependenciesProperty.clear()
        winDependenciesProperty.addAll()
    }

    @XmlTransient
    val linuxDependenciesProperty = SimpleListProperty<JvmDependency>(FXCollections.observableArrayList())
    @XmlElementWrapper(name = "linux")
    @XmlElement(name = "dependency")
    fun getLinuxDependencies() = linuxDependenciesProperty.get()

    fun setLinuxDependencies(value : List<JvmDependency>) {
        linuxDependenciesProperty.clear()
        linuxDependenciesProperty.addAll(value)
    }


    fun addPlatformSpecificDependency(dependencies: List<JvmDependency>, os: OperatingSystem) {
        when (os) {
            OperatingSystem.MAC_OS_X -> macDependenciesProperty.addAll(dependencies)
            OperatingSystem.WINDOWS -> winDependenciesProperty.addAll(dependencies)
            OperatingSystem.LINUX -> linuxDependenciesProperty.addAll(dependencies)
        }
    }

    fun isPlatformSpecificDependency(dependency: JvmDependency) : Boolean {
        return (macDependenciesProperty.contains(dependency)) || winDependenciesProperty.contains(dependency) || linuxDependenciesProperty.contains(dependency)
    }
}