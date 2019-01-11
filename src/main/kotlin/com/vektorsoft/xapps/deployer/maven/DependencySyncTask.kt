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

package com.vektorsoft.xapps.deployer.maven

import com.vektorsoft.xapps.deployer.logger
import com.vektorsoft.xapps.deployer.model.MavenDependency
import com.vektorsoft.xapps.deployer.model.Project
import javafx.concurrent.Task

class DependencySyncTask(val project : Project) : Task<Unit>() {

    val LOGGER by logger(DependencySyncTask::class.java)

    override fun call() {
        val result = MavenHandler.buildProject(project)
        if(!result) {
            LOGGER.warn("Failed to build project $project. Dependencies might not be up to date.")
        }
        val depList = MavenHandler.listDependencies(project)
        val projectDeps = mutableListOf<MavenDependency>()
        depList.forEach {
            projectDeps.add(MavenHandler.resolveDependency(it, project) ?: return@forEach)
        }

        project.application.jvm.dependencies = projectDeps.filter { !project.application.jvm.platformDependencies.isPlatformSpecificDependency(it) }

    }
}