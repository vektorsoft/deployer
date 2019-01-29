/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ui

import com.vektorsoft.xapps.deployer.ctrl.*
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
    const val DEPENDENCY_INFO_PANE = "dependency_info_pane"
    const val PLATFORM_DEPENDENCY_PANE = "platform_dependency_pane"
    const val ADD_MAVEN_DEPENDENCY_PANE = "add_maven_dependency_pane"
    const val ADD_SERVER_PANE = "add_server_pane"


    private val componentMap = mutableMapOf<String, Parent>()
    private lateinit var mainWindow : Stage

    fun loadComponents() {
        val bundle = ResourceBundle.getBundle("i18n/strings")
        componentMap[START_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/start-pane.fxml"), bundle)
        loadWithController("/fxml/new-project-dlg.fxml", NewProjectDialogController::class.java, NEW_PROJECT_DLG, bundle)
        loadWithController("/fxml/project-info.fxml", ProjectInfoController::class.java, PROJECT_INFO_PANE, bundle)
        componentMap[PROJECT_BUTTON_BAR] = FXMLLoader.load(javaClass.getResource("/fxml/project-button-bar.fxml"))
        componentMap[APP_INFO_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/app-info.fxml"))
        componentMap[DEPENDENCY_INFO_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/dependency-info.fxml"))
        componentMap[PLATFORM_DEPENDENCY_PANE] = FXMLLoader.load(javaClass.getResource("/fxml/platform-deps-info.fxml"))
        loadWithController("/fxml/maven-dependency-pane.fxml", NewMavenDependencyController::class.java, ADD_MAVEN_DEPENDENCY_PANE, bundle)
        loadWithController("/fxml/new-server.fxml", AddServerController::class.java, ADD_SERVER_PANE, bundle)

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