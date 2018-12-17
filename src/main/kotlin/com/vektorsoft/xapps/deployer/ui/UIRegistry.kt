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
import javafx.stage.Stage
import java.util.*

object UIRegistry {

    const val MAIN_PAGE = "main_page"
    const val START_PANE = "start_pane"
    const val NEW_PROJECT_DLG = "new_project_dlg"
    const val PROJECT_INFO_PANE = "project_info_pane"
    const val PROJECT_BUTTON_BAR = "project_button_bar"
    const val APP_INFO_PANE = "app_info_pane"


    private val componentMap = mutableMapOf<String, Parent>()
    private lateinit var mainWindow : Stage

    fun loadComponents() {
        val bundle = ResourceBundle.getBundle("i18n/strings")
        componentMap[START_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/start-pane.fxml"), bundle)
        loadWithController("/fxml/new-project-dlg.fxml", NewProjectDialogController::class.java, NEW_PROJECT_DLG, bundle)
        loadWithController("/fxml/project-info.fxml", ProjectInfoController::class.java, PROJECT_INFO_PANE, bundle)
        componentMap[PROJECT_BUTTON_BAR] = FXMLLoader.load(javaClass.getResource("/fxml/project-button-bar.fxml"))
        componentMap[APP_INFO_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/app-info.fxml"))

        // load main window last, to make sure all children are loaded
        componentMap[MAIN_PAGE] = FXMLLoader.load<Parent>(javaClass.getResource("/fxml/main.fxml"))
    }

    private fun <T> loadWithController(fxmlFile : String, clazz : Class<T>, key : String, bundle : ResourceBundle) {
        val loader = FXMLLoader(javaClass.getResource(fxmlFile))
        loader.resources = bundle
        loader.setController(ControllerRegistry.getController(clazz))

        componentMap[key] = loader.load()
    }

    fun getComponent(name : String) : Parent? = componentMap[name]

    fun setMainWindow(stage : Stage) { mainWindow = stage }
    fun getMainWindow() : Stage =   mainWindow
}