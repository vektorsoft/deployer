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

object ControllerRegistry {

    private val controllerMap = mutableMapOf<String, Any>()

    fun registerControllers() {
        controllerMap[NewProjectDialogController::class.java.name] = NewProjectDialogController()
        controllerMap[ProjectInfoController::class.java.name] = ProjectInfoController()
        controllerMap[NewMavenDependencyController::class.java.name] = NewMavenDependencyController()
    }

    fun <T>  getController(clazz : Class<T>) : T = controllerMap[clazz.name] as T
}