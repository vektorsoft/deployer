/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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