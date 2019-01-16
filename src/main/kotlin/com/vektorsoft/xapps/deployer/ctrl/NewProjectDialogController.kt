/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.model.DependencyManagementType
import com.vektorsoft.xapps.deployer.model.Project
import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.util.Callback

class NewProjectDialogController {

    @FXML
    private lateinit  var projectNameField : TextField
    @FXML
    private lateinit var projectLocationField : TextField
    @FXML
    private lateinit var dependencyCombo : ComboBox<DependencyManagementType>

    @FXML
    fun initialize() {
        dependencyCombo.items?.addAll(DependencyManagementType.values())
        dependencyCombo.selectionModel?.selectFirst()
    }

    fun getResultConverter() : Callback<ButtonType, Project?> {
        return object : Callback<ButtonType, Project?> {
            override fun call(btype: ButtonType?): Project? {
                if(btype == ButtonType.OK) {
                    return createProject()
                }
                return null
            }
        }
    }

    fun validateInput() : Boolean {
        return (projectLocationField.text?.isNotEmpty() == true && projectNameField?.text?.isNotEmpty() == true)
    }

    private fun createProject() : Project {
         val project = Project()
        project.name = projectNameField.text
        project.location = projectLocationField.text
        project.dependencyMgmtType = dependencyCombo.selectionModel?.selectedItem
        return project
    }


}
