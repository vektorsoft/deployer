/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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