/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl


import com.vektorsoft.xapps.deployer.logger
import com.vektorsoft.xapps.deployer.maven.MavenHandler
import com.vektorsoft.xapps.deployer.model.JvmDependency
import com.vektorsoft.xapps.deployer.model.ProjectItemType
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import com.vektorsoft.xapps.deployer.ui.fillMavenDependencyTable
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox


class PlatformDependenciesController : ChangeListener<ProjectTreeItem> {

    private val logger by logger(PlatformDependenciesController::class.java)

    @FXML
    private lateinit var macDependencyTable : TableView<JvmDependency>
    @FXML
    private lateinit var linuxDependencyTable : TableView<JvmDependency>
    @FXML
    private lateinit var winDependencyTable : TableView<JvmDependency>
    @FXML
    private lateinit var platformsTabPane : TabPane


    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)

        macDependencyTable.selectionModel.selectionMode = SelectionMode.MULTIPLE
        winDependencyTable.selectionModel.selectionMode = SelectionMode.MULTIPLE
        linuxDependencyTable.selectionModel.selectionMode = SelectionMode.MULTIPLE

    }

    override fun changed(p0: ObservableValue<out ProjectTreeItem>?, p1: ProjectTreeItem?, newItem: ProjectTreeItem?) {
        val project = newItem?.project ?: return
        if (newItem?.type == ProjectItemType.PLATFORM_DEPENDENCIES) {
            fillMavenDependencyTable(project.application.jvm.platformDependencies.macDependenciesProperty, macDependencyTable)
            fillMavenDependencyTable(project.application.jvm.platformDependencies.linuxDependenciesProperty, linuxDependencyTable)
            fillMavenDependencyTable(project.application.jvm.platformDependencies.winDependenciesProperty, winDependencyTable)
        }
    }

    @FXML
    fun newMavenDependency() {
        val dialog = Dialog<String?>()
        dialog.title = "Add Maven Dependency"
        dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)
        dialog.dialogPane.content = UIRegistry.getComponent(UIRegistry.ADD_MAVEN_DEPENDENCY_PANE)
        dialog.resultConverter = ControllerRegistry.getController(NewMavenDependencyController::class.java).getResultConverter()
        dialog.dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION,  {
            if(!ControllerRegistry.getController(NewMavenDependencyController::class.java).validateInput()) {
                it.consume()
            }
        })


        val result = dialog.showAndWait()
        println("dialog closed")
        Platform.runLater {
            val project = RuntimeData.selectedProjectItem.get().project ?: null
            if(project != null && result != null) {
                val dependency = MavenHandler.resolveDependency(result.get(), project)
                logger.debug("Succesfully downloaded dependency {}", result.get())
                val selectedId = platformsTabPane.selectionModel.selectedItem.id
                when(selectedId) {
                    "windowsTab" -> project?.application?.jvm?.platformDependencies?.winDependenciesProperty?.add(dependency)
                    "macTab" -> project?.application?.jvm?.platformDependencies?.macDependenciesProperty?.add(dependency)
                    "linuxTab" -> project?.application?.jvm?.platformDependencies?.linuxDependenciesProperty?.add(dependency)
                }
            }
        }

    }

    @FXML
    fun removeDependency() {
        val selectedId = platformsTabPane.selectionModel.selectedItem.id
        when(selectedId) {
            "windowsTab" -> removeSelectedDependency(winDependencyTable)
            "macTab" -> removeSelectedDependency(macDependencyTable)
            "linuxTab" -> removeSelectedDependency(linuxDependencyTable)
        }
    }

    private fun removeSelectedDependency(table : TableView<JvmDependency>) {
        table.itemsProperty().get().removeAll(table.selectionModel.selectedItems)
    }
}