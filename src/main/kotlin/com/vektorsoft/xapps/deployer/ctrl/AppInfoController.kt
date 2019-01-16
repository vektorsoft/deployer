/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.filePathRelative
import com.vektorsoft.xapps.deployer.model.BinaryData
import com.vektorsoft.xapps.deployer.model.ProjectItemType
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.model.Server
import com.vektorsoft.xapps.deployer.ui.IconBar
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import java.io.File

class AppInfoController : ChangeListener<ProjectTreeItem> {
    @FXML
    private lateinit var appNameField : TextField
    @FXML
    private lateinit var appVersionField : TextField
    @FXML
    private lateinit var appDescriptionArea : TextArea
    @FXML
    private lateinit var serverCombo : ComboBox<Server>
    @FXML
    private lateinit var iconsBarArea : VBox



    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)

    }

    override fun changed(observable: ObservableValue<out ProjectTreeItem>?, oldValue: ProjectTreeItem?, newValue: ProjectTreeItem?) {

        if(oldValue?.type == ProjectItemType.APPLICATION) {
            appNameField.textProperty().unbindBidirectional(oldValue.project?.application?.info?.appNameProperty)
            appVersionField.textProperty().unbindBidirectional(oldValue.project?.application?.versionProperty)
            appDescriptionArea.textProperty().unbindBidirectional(oldValue.project?.application?.info?.descriptionProperty)
            iconsBarArea.children.remove(1, iconsBarArea.children.size) // remove children except 'Add' button
        }
        if(newValue?.type == ProjectItemType.APPLICATION) {
            appNameField.textProperty().bindBidirectional(newValue.project?.application?.info?.appNameProperty)
            appVersionField.textProperty().bindBidirectional(newValue.project?.application?.versionProperty)
            appDescriptionArea.textProperty().bindBidirectional(newValue.project?.application?.info?.descriptionProperty)
            for(binData in newValue.project?.application?.info?.icons ?: return) {
                iconsBarArea.children.add(IconBar(binData.path, newValue.project?.location ?: "", iconsBarArea))
            }
        }
    }

    @FXML
    fun addIcon() {
        val fileChooser = FileChooser()
        fileChooser.title = "Choose icons"
        fileChooser.initialDirectory = File(RuntimeData.selectedProjectItem.get().project?.location)
        fileChooser.extensionFilters.addAll(iconExtensionFilters())
        val selectedFiles = fileChooser.showOpenMultipleDialog(UIRegistry.getMainWindow())
        val projectLocation = RuntimeData.selectedProjectItem.get().project?.location ?: ""


        for(file in selectedFiles ?: return) {
            iconsBarArea.children.add(IconBar(file.absolutePath, projectLocation, iconsBarArea))
            RuntimeData.selectedProjectItem.get().project?.application?.info?.addIcon(BinaryData(filePathRelative(file.absolutePath, projectLocation), file.name, "123334455", file.length()))
        }
    }

    private fun iconExtensionFilters() : List<FileChooser.ExtensionFilter> {
        return listOf(
            FileChooser.ExtensionFilter("All Icons", "*.png", "*.ico", "*.icns"),
            FileChooser.ExtensionFilter("PNG", ".png"),
            FileChooser.ExtensionFilter("ICO", "*.ico"),
            FileChooser.ExtensionFilter("ICNS", "*.icns")
        )
    }

}