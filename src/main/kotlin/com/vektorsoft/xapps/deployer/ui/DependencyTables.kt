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
import javafx.collections.FXCollections
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory

fun <T : JvmDependency> fillMavenDependencyTable(dependencies : List<T>, table : TableView<MavenDependency>) {

    val groupCol = TableColumn<MavenDependency, String>("Group ID")
    groupCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("groupId")
    val artifactCol = TableColumn<MavenDependency, String>("Artifact ID")
    artifactCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("artifactId")
    val versionCol = TableColumn<MavenDependency, String>("Version")
    versionCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("version")
    val packagingCol = TableColumn<MavenDependency, String>("Packaging")
    packagingCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("packaging")
    val classifierCol = TableColumn<MavenDependency, String>("Classifier")
    classifierCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("classifier")
    val scopeCol = TableColumn<MavenDependency, JvmDependencyScope>("Scope")
    scopeCol.setCellValueFactory { it -> it.value.scopeProperty }
    scopeCol.cellFactory = ComboBoxTableCell.forTableColumn(JvmDependencyScope.CLASSPATH, JvmDependencyScope.MODULE_PATH)
    table.columns.clear()

    table.columns.addAll(groupCol, artifactCol, versionCol, packagingCol, classifierCol, scopeCol)
    table.items = FXCollections.observableList(dependencies.map { it as MavenDependency })

}