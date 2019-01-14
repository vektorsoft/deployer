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

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.maven.DependencySyncTask
import com.vektorsoft.xapps.deployer.model.*
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import com.vektorsoft.xapps.deployer.ui.fillMavenDependencyTable
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox


class DependencyController : ChangeListener<ProjectTreeItem> {

    @FXML
    private lateinit var progressPane: BorderPane
    @FXML
    private lateinit var dependenciesPane: AnchorPane
    @FXML
    private lateinit var syncProgressIndicator: ProgressIndicator
    @FXML
    private lateinit var dependencyTable: TableView<MavenDependency>
    @FXML
    private lateinit var buttonBar: HBox
    @FXML
    private lateinit var platformButton : MenuButton
    @FXML
    private lateinit var osxMenuItem : MenuItem
    @FXML
    private lateinit var windowsMenuItem : MenuItem
    @FXML
    private lateinit var linuxMenuItem : MenuItem

    private val taskStatusProperty = SimpleBooleanProperty()

    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)
        progressPane.toBack()
        dependenciesPane.isVisible = true
        dependencyTable.selectionModel.selectionMode = SelectionMode.MULTIPLE
        dependencyTable.isEditable = true

        dependencyTable.selectionModel.selectedItemProperty().addListener { _, _, newSel ->
            platformButton.disableProperty().value = (newSel == null)
        }

        osxMenuItem.setOnAction { _ -> markDependencyAsPlatform(OperatingSystem.MAC_OS_X) }
        windowsMenuItem.setOnAction { _ -> markDependencyAsPlatform(OperatingSystem.WINDOWS) }
        linuxMenuItem.setOnAction { _ -> markDependencyAsPlatform(OperatingSystem.LINUX) }
    }

    override fun changed(
        observable: ObservableValue<out ProjectTreeItem>?,
        oldItem: ProjectTreeItem?,
        newItem: ProjectTreeItem?
    ) {
        val project = newItem?.project ?: return
        if (newItem?.type == ProjectItemType.DEPENDENCIES) {
            syncDependencies(project)
        }
    }

    private fun syncDependencies(project: Project) {
        if (!project.synced) {
            syncFromPom(project)
        } else {
            fillMavenDependencyTable(project.application.jvm.dependencies, dependencyTable)
        }
    }

    private fun syncFromPom(project: Project) {
        if (!taskStatusProperty.value) {
            val task = DependencySyncTask(project)
            taskStatusProperty.bind(task.runningProperty())
            syncProgressIndicator.progressProperty().bind(task.progressProperty())
            Thread(task).start()
            task.stateProperty().addListener { observableValue, oldState, newState ->
                when (newState) {
                    Worker.State.RUNNING -> {
                        progressPane.toFront()
                        dependenciesPane.isVisible = false
                        buttonBar.isVisible = false
                    }
                    Worker.State.SUCCEEDED -> {
                        fillMavenDependencyTable(project.application.jvm.dependencies, dependencyTable)
                        dependenciesPane.toFront()
                        dependenciesPane.isVisible = true
                        buttonBar.isVisible = true
                        project.synced = true
                    }

                }
            }
        }
    }


    private fun markDependencyAsPlatform(os : OperatingSystem) {
        val selectedItems = dependencyTable.selectionModel.selectedItems
        RuntimeData.selectedProjectItem.get().project?.application?.jvm?.platformDependencies?.addPlatformSpecificDependency(selectedItems, os)
        dependencyTable.items.removeAll(selectedItems)
    }
}