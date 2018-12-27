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

import com.vektorsoft.xapps.deployer.maven.MavenHandler
import com.vektorsoft.xapps.deployer.model.Project
import com.vektorsoft.xapps.deployer.model.ProjectItemType
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.persist.ProjectPersistenceData
import com.vektorsoft.xapps.deployer.persist.XMLPersister
import com.vektorsoft.xapps.deployer.ui.ProjectTreeCellFactory
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane

class MainPageController : ListChangeListener<Project> {

    @FXML
    lateinit private var projectTree : TreeView<ProjectTreeItem>
    @FXML
    lateinit private var detailsPane : BorderPane

    // creadit: <div>Icons made by <a href="https://www.flaticon.com/authors/eucalyp" title="Eucalyp">Eucalyp</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    private val treeRootIcon = ImageView(Image(javaClass.getResourceAsStream("/img/applications16x16.png")))
    private val projectIcon = Image(javaClass.getResourceAsStream("/img/structure_16x16.png"))
    private val dependencyIcon = Image(javaClass.getResourceAsStream("/img/dependencies_16x16.png"))
    private val platformDepIcon = Image(javaClass.getResourceAsStream("/img/platform_deps_16x16.png"))
    private val nativeLibIcon = Image(javaClass.getResourceAsStream("/img/binary_code_16x16.png"))
    private val applicationIcon = Image(javaClass.getResourceAsStream("/img/software_16x16.png"))

    @FXML
    fun initialize() {
        createProjectTree()
        detailsPane.center = UIRegistry.getComponent(UIRegistry.START_PANE)
        RuntimeData.projectList.addListener(this)
    }

    private fun createProjectTree() {
        projectTree.cellFactory = ProjectTreeCellFactory()
        val root = TreeItem<ProjectTreeItem>(ProjectTreeItem(ProjectItemType.ROOT), treeRootIcon)
        root.expandedProperty().set(true)
        projectTree.root = root

        Platform.runLater {
            for(location in ProjectPersistenceData.loadProjectLocations()) {
                val proj = XMLPersister.loadProject(location)
                projectTree.root?.children?.add(createProjectNode(proj))
            }
        }

        projectTree.selectionModel?.selectedItemProperty()?.addListener(object: ChangeListener<TreeItem<ProjectTreeItem>> {
            override fun changed(
                observable: ObservableValue<out TreeItem<ProjectTreeItem>>?,
                oldValue : TreeItem<ProjectTreeItem>?,
                newValue : TreeItem<ProjectTreeItem>?
            ) {
                RuntimeData.selectedProjectItem.value = newValue?.value
                when(newValue?.value?.type) {
                    ProjectItemType.ROOT -> {
                        detailsPane.center =  UIRegistry.getComponent(UIRegistry.START_PANE)
                        detailsPane.bottom = null
                    }
                    ProjectItemType.PROJECT -> {
                        detailsPane.center = UIRegistry.getComponent(UIRegistry.PROJECT_INFO_PANE)
                        detailsPane.bottom = UIRegistry.getComponent(UIRegistry.PROJECT_BUTTON_BAR)
                    }
                    ProjectItemType.APPLICATION -> {
                        detailsPane.center = UIRegistry.getComponent(UIRegistry.APP_INFO_PANE)
                        detailsPane.bottom = UIRegistry.getComponent(UIRegistry.PROJECT_BUTTON_BAR)
                    }
                    ProjectItemType.DEPENDENCIES -> {
                        detailsPane.center = UIRegistry.getComponent(UIRegistry.DEPENDENCY_INFO_PANE)
                        detailsPane.bottom = UIRegistry.getComponent(UIRegistry.PROJECT_BUTTON_BAR)
                    }
                }
            }
        })
    }



    override fun onChanged(change: ListChangeListener.Change<out Project>?) {
        while (change?.next() == true && change.wasAdded() == true) {
            for(project in (change.addedSubList ?: emptyList())) {
                projectTree.root?.children?.add(createProjectNode(project))
                ProjectPersistenceData.saveProject(project)
            }
        }
    }

    private fun createProjectNode(project : Project) : TreeItem<ProjectTreeItem> {
        val projectNode = TreeItem(ProjectTreeItem(ProjectItemType.PROJECT, project), ImageView(projectIcon))
        projectNode.children.add(TreeItem(ProjectTreeItem(ProjectItemType.APPLICATION, project), ImageView(applicationIcon)))
        projectNode.children.add(TreeItem(ProjectTreeItem(ProjectItemType.DEPENDENCIES, project), ImageView(dependencyIcon)))
        projectNode.children.add(TreeItem(ProjectTreeItem(ProjectItemType.PLATFORM_DEPENDENCIES, project), ImageView(platformDepIcon)))
        projectNode.children.add(TreeItem(ProjectTreeItem(ProjectItemType.NATIVE, project), ImageView(nativeLibIcon)))

        return projectNode
    }


}