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

package com.vektorsoft.xapps.deployer.ui

import com.vektorsoft.xapps.deployer.model.JvmDependency
import com.vektorsoft.xapps.deployer.model.JvmDependencyScope
import com.vektorsoft.xapps.deployer.model.MavenDependency
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.ComboBoxTableCell

fun fillMavenDependencyTable(dependencies : ListProperty<JvmDependency>, table : TableView<JvmDependency>) {


    val groupCol = TableColumn<JvmDependency, String>("Group ID")
    groupCol.setCellValueFactory { SimpleObjectProperty<String>((it.value as MavenDependency).groupId) }
    val artifactCol = TableColumn<JvmDependency, String>("Artifact ID")
    artifactCol.setCellValueFactory { SimpleObjectProperty<String>((it.value as MavenDependency).artifactId) }
    val versionCol = TableColumn<JvmDependency, String>("Version")
    versionCol.setCellValueFactory { SimpleObjectProperty<String>((it.value as MavenDependency).version) }
    val packagingCol = TableColumn<JvmDependency, String>("Packaging")
    packagingCol.setCellValueFactory { SimpleObjectProperty<String>((it.value as MavenDependency).packaging) }
    val classifierCol = TableColumn<JvmDependency, String>("Classifier")
    classifierCol.setCellValueFactory { SimpleObjectProperty<String>((it.value as MavenDependency).classifier) }
    val scopeCol = TableColumn<JvmDependency, JvmDependencyScope>("Scope")
    scopeCol.setCellValueFactory { it -> it.value.scopeProperty }
    scopeCol.cellFactory = ComboBoxTableCell.forTableColumn(JvmDependencyScope.CLASSPATH, JvmDependencyScope.MODULE_PATH)
    table.columns.clear()

    table.columns.addAll(groupCol, artifactCol, versionCol, packagingCol, classifierCol, scopeCol)
    table.itemsProperty().bindBidirectional(dependencies)
}