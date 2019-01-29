/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.persist

import com.vektorsoft.xapps.deployer.model.Project
import com.vektorsoft.xapps.deployer.model.Server
import java.util.*
import java.util.prefs.Preferences

object ProjectPersistenceData {

    const val DEPLOYER_PROJECTS_ROOT = "com.vektorsoft.xapps.deployer.pojects"
    const val DEPLOYER_SERVERS_ROOT = "com.vektorsoft.xapps.deployer.servers"
    const val LOCATION_KEY = "location"
    const val NAME_KEY = "name"
    const val URL_KEY = "url"

    fun saveProject(project : Project) {
        val rootNode = Preferences.userRoot().node(DEPLOYER_PROJECTS_ROOT)
        val projectNode = rootNode.node(escapeSlashes(project.location))
        projectNode.put(LOCATION_KEY, project.location)
        projectNode.put(NAME_KEY, project.name)
    }

    fun loadProjectLocations() : List<String> {
        val locations = mutableListOf<String>()
        val rootNode = Preferences.userRoot().node(DEPLOYER_PROJECTS_ROOT)
        for(location in rootNode.childrenNames()) {
            val projectNode = rootNode.node(location)
            val loc = projectNode.get(LOCATION_KEY, "")
            if(loc.isNotEmpty()) {
                locations.add(loc)
            }

        }
        return locations
    }

    fun saveServer(server : Server) {
        val serversRoot = Preferences.userRoot().node(DEPLOYER_SERVERS_ROOT)
        val uuid = UUID.randomUUID().toString()
        val serverNode = serversRoot.node(uuid)
        serverNode.put(NAME_KEY, server.name)
        serverNode.put(URL_KEY, server.baseUrl)
    }

    fun loadServers() : List<Server> {
        val list = mutableListOf<Server>()
        val serversRoot = Preferences.userRoot().node(DEPLOYER_SERVERS_ROOT)
        for(srvId in serversRoot.childrenNames()) {
            val serverNode = serversRoot.node(srvId)
            val server = Server()
            server.name = serverNode.get(NAME_KEY, " ")
            server.baseUrl = serverNode.get(URL_KEY, "")
            list.add(server)
        }
        return list
    }

    private fun escapeSlashes(value : String?) : String {
        return value?.replace("/", "___") ?: ""
    }
}