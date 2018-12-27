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
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.ScrollPane
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox


class DependencyController : ChangeListener<ProjectTreeItem> {

    @FXML
    private lateinit var progressPane : BorderPane
    @FXML
    private lateinit var dependenciesPane : ScrollPane
    @FXML
    private lateinit var syncProgressIndicator : ProgressIndicator
    @FXML
    private lateinit var dependencyTable : TableView<MavenDependency>
    @FXML
    private lateinit var buttonBar : HBox

    private val taskStatusProperty = SimpleBooleanProperty()

    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)
        progressPane.toBack()
    }

    override fun changed(observable: ObservableValue<out ProjectTreeItem>?, oldItem: ProjectTreeItem?, newItem: ProjectTreeItem?) {
        val project = newItem?.project ?: return
        if(newItem?.type == ProjectItemType.DEPENDENCIES && project.application.jvm.dependencies.isEmpty()) {
            if(!taskStatusProperty.value) {
                val task = DependencySyncTask(project)
                taskStatusProperty.bind(task.runningProperty())
                syncProgressIndicator.progressProperty().bind(task.progressProperty())
                Thread(task).start()
                task.stateProperty().addListener { observableValue, oldState, newState ->
                    when(newState) {
                        Worker.State.RUNNING -> {
                            progressPane.toFront()
                            dependenciesPane.isVisible = false
                            buttonBar.isVisible = false
                        }
                        Worker.State.SUCCEEDED -> {
                            fillDependencyTable(project)
                            dependenciesPane.toFront()
                            dependenciesPane.isVisible = true
                            buttonBar.isVisible = true
                        }

                    }
                }
            }


        }
    }

    private fun fillDependencyTable(project: Project) {
        val deps = project.application.jvm.dependencies

        val groupCol = TableColumn<MavenDependency, String>("Group ID")
        groupCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("groupId")
        val artifactCol = TableColumn<MavenDependency, String>("Artifact ID")
        artifactCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("artifactId")
        val versionCol = TableColumn<MavenDependency, String>("Version")
        versionCol.cellValueFactory = PropertyValueFactory<MavenDependency, String>("version")
        dependencyTable.columns.clear()

        dependencyTable.columns.addAll(groupCol, artifactCol, versionCol)
        dependencyTable.items = FXCollections.observableList(deps.map { it as MavenDependency })

    }
}