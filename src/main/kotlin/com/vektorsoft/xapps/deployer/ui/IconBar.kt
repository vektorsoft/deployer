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

package com.vektorsoft.xapps.deployer.ui

import com.vektorsoft.xapps.deployer.filePathRelative
import com.vektorsoft.xapps.deployer.model.RuntimeData
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import java.io.File
import java.nio.file.Paths

class IconBar(iconFilePath : String, projectLocation : String, parent : VBox) : HBox(15.0) {

    private val ICON_WIDTH = 32.0
    private val ICON_HEIGHT = 32.0
    private val removeButton : Button

    init {
        alignment = Pos.CENTER_LEFT
        children.add(ImageView(Image(getIconFile(iconFilePath, projectLocation).inputStream(), ICON_WIDTH, ICON_HEIGHT, false, false)))
        val pathField = TextField(filePathRelative(iconFilePath, projectLocation))
        pathField.editableProperty().set(false)
        pathField.prefWidth(80.0)
        children.add(pathField)

        removeButton = Button("Remove")
        removeButton.onAction = EventHandler<ActionEvent> {
            parent.children.remove(this)
            RuntimeData.selectedProjectItem.get().project?.application?.info?.removeIcon(iconFilePath)
        }
        children.add(removeButton)
    }



    private fun getIconFile(path : String, projectLocation: String) : File {
        val p = Paths.get(path)
        if(p.isAbsolute) {
            return p.toFile()
        } else {
            return File(projectLocation, path)
        }
    }

}