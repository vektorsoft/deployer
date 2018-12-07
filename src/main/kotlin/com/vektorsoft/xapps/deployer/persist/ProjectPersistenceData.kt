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

package com.vektorsoft.xapps.deployer.persist

import com.vektorsoft.xapps.deployer.model.Project
import java.util.prefs.Preferences

object ProjectPersistenceData {

    private const val DEPLOYER_PREF_ROOT = "com.vektorsoft.xapps.deployer.pojects"

    fun saveProject(project : Project) {
        val rootNode = Preferences.userRoot().node(DEPLOYER_PREF_ROOT)
        rootNode.put(project.name, project.name)
        val projectNode = rootNode.node(project.name)
        projectNode.put("location", project.location)
        println("Saved project")
    }

    fun loadProjects() : List<Project> {
        val projects = mutableListOf<Project>()
        val rootNode = Preferences.userRoot().node(DEPLOYER_PREF_ROOT)
        for(name in rootNode.childrenNames()) {
            val project = Project()
            project.name = name
            val projectNode = rootNode.node(name)
            project.location = projectNode.get("location", "")
            projects.add(project)
        }
        return projects
    }
}