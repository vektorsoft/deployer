/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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