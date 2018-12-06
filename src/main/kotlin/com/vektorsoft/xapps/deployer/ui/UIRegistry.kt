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

import com.vektorsoft.xapps.deployer.ctrl.ControllerRegistry
import com.vektorsoft.xapps.deployer.ctrl.NewProjectDialogController
import com.vektorsoft.xapps.deployer.ctrl.ProjectInfoController
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

object UIRegistry {

    const val MAIN_PAGE = "main_page"
    const val START_PANE = "start_pane"
    const val NEW_PROJECT_DLG = "new_project_dlg"
    const val PROJECT_INFO_PANE = "project_info_pane"

    private val componentMap = mutableMapOf<String, Parent>()

    fun loadComponents() {
        componentMap[START_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/start-pane.fxml"))
        loadWithController("/fxml/new-project-dlg.fxml", NewProjectDialogController::class.java, NEW_PROJECT_DLG)
        loadWithController("/fxml/project-info.fxml", ProjectInfoController::class.java, PROJECT_INFO_PANE)

        // load main window last, to make sure all children are loaded
        componentMap[MAIN_PAGE] = FXMLLoader.load<Parent>(javaClass.getResource("/fxml/main.fxml"))
    }

    private fun <T> loadWithController(fxmlFile : String, clazz : Class<T>, key : String) {
        val loader = FXMLLoader(javaClass.getResource(fxmlFile))
        loader.setController(ControllerRegistry.getController(clazz))
        componentMap[key] = loader.load()
    }

    fun getComponent(name : String) : Parent? = componentMap[name]
}