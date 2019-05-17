/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer

import com.vektorsoft.xapps.deployer.ctrl.ControllerRegistry
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import java.awt.SplashScreen


class Deployer : Application() {

    override fun start(stage : Stage?) {
        ControllerRegistry.registerControllers()
        UIRegistry.loadComponents()
        UIRegistry.setMainWindow(stage ?: throw Exception("Stage can not be null"))

        val scene = Scene(UIRegistry.getComponent(UIRegistry.MAIN_PAGE), 900.0, 700.0)
        stage.title = "Deployer"
        stage.scene = scene
        stage.show()

        val splash = SplashScreen.getSplashScreen() ?: return
        splash.close()
    }
}

fun main(args : Array<String>) {
    Application.launch(Deployer::class.java, *args)
}