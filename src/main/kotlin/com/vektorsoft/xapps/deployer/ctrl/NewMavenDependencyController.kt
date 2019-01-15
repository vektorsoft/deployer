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

import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.util.Callback

class NewMavenDependencyController {

    @FXML
    private lateinit var groupIdTextField : TextField
    @FXML
    private lateinit var artifactIdTextField : TextField
    @FXML
    private lateinit var versionTextField : TextField
    @FXML
    private lateinit var classifierTextField : TextField
    @FXML
    private lateinit var packagingCombo : ComboBox<String>

    @FXML
    fun initialize() {
        packagingCombo.items.addAll("jar", "zip")
        packagingCombo.selectionModel.select("jar")
    }



    fun getResultConverter() : Callback<ButtonType, String?> {
        return object : Callback<ButtonType, String?> {
            override fun call(btype: ButtonType?): String? {
                if(btype == ButtonType.OK) {
                    return createDependency()
                }
                return null
            }
        }
    }

    private fun createDependency() : String {
       val sb = StringBuilder(groupIdTextField.text)
           .append(":").append(artifactIdTextField.text)
           .append(":").append(packagingCombo.selectionModel.selectedItem)
        if(classifierTextField.text.isNotEmpty()) {
            sb.append(":").append(classifierTextField.text)
        }
        sb.append(":").append(versionTextField.text)
        sb.append(":").append("compile")

        return sb.toString()
    }

    fun validateInput() : Boolean {
        return groupIdTextField.text.isNotEmpty() &&
                artifactIdTextField.text.isNotEmpty() &&
                versionTextField.text.isNotEmpty() &&
                classifierTextField.text.isNotEmpty()
    }

}