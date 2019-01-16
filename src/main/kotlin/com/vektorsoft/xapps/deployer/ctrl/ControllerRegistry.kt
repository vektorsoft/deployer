/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

object ControllerRegistry {

    private val controllerMap = mutableMapOf<String, Any>()

    fun registerControllers() {
        controllerMap[NewProjectDialogController::class.java.name] = NewProjectDialogController()
        controllerMap[ProjectInfoController::class.java.name] = ProjectInfoController()
        controllerMap[NewMavenDependencyController::class.java.name] = NewMavenDependencyController()
    }

    fun <T>  getController(clazz : Class<T>) : T = controllerMap[clazz.name] as T
}