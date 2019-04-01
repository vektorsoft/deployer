/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

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
import javafx.scene.layout.AnchorPane

class MainPageController : ListChangeListener<Project> {

    private val ANCHOR_DISTANCE = 10.0

    @FXML
    lateinit private var projectTree : TreeView<ProjectTreeItem>
    @FXML
    lateinit private var detailsPane : AnchorPane

    // creadit: <div>Icons made by <a href="https://www.flaticon.com/authors/eucalyp" title="Eucalyp">Eucalyp</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    private val treeRootIcon = ImageView(Image(javaClass.getResourceAsStream("/img/applications16x16.png")))
    private val projectIcon = Image(javaClass.getResourceAsStream("/img/structure_16x16.png"))
    private val dependencyIcon = Image(javaClass.getResourceAsStream("/img/dependencies_16x16.png"))
    private val platformDepIcon = Image(javaClass.getResourceAsStream("/img/platform_deps_16x16.png"))
    private val nativeLibIcon = Image(javaClass.getResourceAsStream("/img/binary_code_16x16.png"))
    private val applicationIcon = Image(javaClass.getResourceAsStream("/img/software_16x16.png"))
    private val jvmIcon = Image(javaClass.getResourceAsStream("/img/jvm_16x16.png"))

    @FXML
    fun initialize() {
        createProjectTree()
        detailsPane.children.add(UIRegistry.getComponent(UIRegistry.START_PANE))
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
                        detailsPane.children.clear()
                        detailsPane.children.add(UIRegistry.getComponent(UIRegistry.START_PANE))
                        AnchorPane.setTopAnchor(detailsPane.children[0], 0.0)
                        AnchorPane.setLeftAnchor(detailsPane.children[0], 0.0)
                        AnchorPane.setRightAnchor(detailsPane.children[0], 0.0)
//                        detailsPane.bottom = null
                    }
                    ProjectItemType.PROJECT -> {
                        setupDetailsPane(UIRegistry.PROJECT_INFO_PANE)
                    }
                    ProjectItemType.APPLICATION -> {
                        setupDetailsPane(UIRegistry.APP_INFO_PANE)
                    }
                    ProjectItemType.DEPENDENCIES -> {
                        setupDetailsPane(UIRegistry.DEPENDENCY_INFO_PANE)
                    }
                    ProjectItemType.PLATFORM_DEPENDENCIES -> {
                        setupDetailsPane(UIRegistry.PLATFORM_DEPENDENCY_PANE)
                    }
                    ProjectItemType.JVM -> {
                        setupDetailsPane(UIRegistry.JVM_PROPERTIES_PANE)
                    }
                }
            }
        })
    }

    private fun setupDetailsPane(name : String) {
        detailsPane.children.clear()
        detailsPane.children.addAll(UIRegistry.getComponent(name), UIRegistry.getComponent(UIRegistry.PROJECT_BUTTON_BAR))
        AnchorPane.setTopAnchor(detailsPane.children[0], ANCHOR_DISTANCE)
        AnchorPane.setLeftAnchor(detailsPane.children[0], ANCHOR_DISTANCE)
        AnchorPane.setRightAnchor(detailsPane.children[0], ANCHOR_DISTANCE)
        AnchorPane.setBottomAnchor(detailsPane.children[1], ANCHOR_DISTANCE)
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
        val jvmTreeItem = TreeItem(ProjectTreeItem(ProjectItemType.JVM, project), ImageView(jvmIcon))
        projectNode.children.add(jvmTreeItem)
        jvmTreeItem.children.add(TreeItem(ProjectTreeItem(ProjectItemType.DEPENDENCIES, project), ImageView(dependencyIcon)))
        jvmTreeItem.children.add(TreeItem(ProjectTreeItem(ProjectItemType.PLATFORM_DEPENDENCIES, project), ImageView(platformDepIcon)))
        projectNode.children.add(TreeItem(ProjectTreeItem(ProjectItemType.NATIVE, project), ImageView(nativeLibIcon)))

        return projectNode
    }


}