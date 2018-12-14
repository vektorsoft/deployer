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

package com.vektorsoft.xapps.deployer

import com.vektorsoft.xapps.deployer.ctrl.ControllerRegistry
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.lang.Exception


class Deployer : Application() {

    override fun start(stage : Stage?) {
        ControllerRegistry.registerControllers()
        UIRegistry.loadComponents()
        UIRegistry.setMainWindow(stage ?: throw Exception("Stage can not be null"))

        val scene = Scene(UIRegistry.getComponent(UIRegistry.MAIN_PAGE), 800.0, 600.0)
        stage.title = "Deployer"
        stage.scene = scene
        stage.show()
    }
}

fun main(args : Array<String>) {
    Application.launch(Deployer::class.java, *args)
}