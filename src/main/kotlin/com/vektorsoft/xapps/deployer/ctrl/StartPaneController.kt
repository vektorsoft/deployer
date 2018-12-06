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

import com.vektorsoft.xapps.deployer.model.Project
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog

class StartPaneController {
    @FXML
    private var newProjectButton : Button? = null

    @FXML
    private var openProjectButton : Button? = null

    @FXML
    fun createProject() {

        val dialog = Dialog<Project?>()
        dialog.title = "Create New Project"
        dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)
        dialog.dialogPane.content = UIRegistry.getComponent(UIRegistry.NEW_PROJECT_DLG)
        dialog.resultConverter = ControllerRegistry.getController(NewProjectDialogController::class.java).getResultConverter()
        dialog.dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION,  {
            if(!ControllerRegistry.getController(NewProjectDialogController::class.java).validateInput()) {
                it.consume()
            }
        })


        val result = dialog.showAndWait()
        if (result.isPresent) {
            RuntimeData.projectList.add(result.get())
        }
    }

}