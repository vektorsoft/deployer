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

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.io.File

class IconBar(iconFile : File) : HBox(15.0) {

    init {
        alignment = Pos.CENTER_LEFT
        children.add(ImageView(Image(iconFile.inputStream(), 32.0, 32.0, false, false)))
        val pathField = TextField(iconFile.absolutePath)
        pathField.editableProperty().set(false)
        pathField.autosize()
        children.add(pathField)
        val removeButton = Button("Remove")
        children.add(removeButton)
    }
}