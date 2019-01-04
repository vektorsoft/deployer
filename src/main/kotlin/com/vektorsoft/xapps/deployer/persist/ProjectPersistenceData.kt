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

    const val DEPLOYER_PREF_ROOT = "com.vektorsoft.xapps.deployer.pojects"
    const val LOCATION_KEY = "location"
    const val NAME_KEY = "name"

    fun saveProject(project : Project) {
        val rootNode = Preferences.userRoot().node(DEPLOYER_PREF_ROOT)
        val projectNode = rootNode.node(escapeSlashes(project.location))
        projectNode.put(LOCATION_KEY, project.location)
        projectNode.put(NAME_KEY, project.name)
    }

    fun loadProjectLocations() : List<String> {
        val locations = mutableListOf<String>()
        val rootNode = Preferences.userRoot().node(DEPLOYER_PREF_ROOT)
        for(location in rootNode.childrenNames()) {
            val projectNode = rootNode.node(location)
            val loc = projectNode.get(LOCATION_KEY, "")
            if(loc.isNotEmpty()) {
                locations.add(loc)
            }

        }
        return locations
    }

    private fun escapeSlashes(value : String?) : String {
        return value?.replace("/", "___") ?: ""
    }
}