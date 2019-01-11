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

package com.vektorsoft.xapps.deployer.ctrl


import com.vektorsoft.xapps.deployer.model.MavenDependency
import com.vektorsoft.xapps.deployer.model.ProjectItemType
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import com.vektorsoft.xapps.deployer.ui.fillMavenDependencyTable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView


class PlatformDependenciesController : ChangeListener<ProjectTreeItem> {

    @FXML
    private lateinit var macDependencyTable : TableView<MavenDependency>
    @FXML
    private lateinit var linuxDependencyTable : TableView<MavenDependency>
    @FXML
    private lateinit var winDependencyTable : TableView<MavenDependency>

    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)

        macDependencyTable.selectionModel.selectionMode = SelectionMode.MULTIPLE
        macDependencyTable.isEditable = true
    }

    override fun changed(p0: ObservableValue<out ProjectTreeItem>?, p1: ProjectTreeItem?, newItem: ProjectTreeItem?) {
        val project = newItem?.project ?: return
        if (newItem?.type == ProjectItemType.PLATFORM_DEPENDENCIES) {
            fillMavenDependencyTable(project.application.jvm.platformDependencies.getMacDependencies(), macDependencyTable)
            fillMavenDependencyTable(project.application.jvm.platformDependencies.getLinuxDependencies(), linuxDependencyTable)
            fillMavenDependencyTable(project.application.jvm.platformDependencies.getWindowsDependencies(), winDependencyTable)
        }
    }
}