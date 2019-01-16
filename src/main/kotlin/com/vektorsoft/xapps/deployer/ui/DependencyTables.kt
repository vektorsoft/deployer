/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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