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
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.prefs.Preferences
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProjectPersistenseDataTest {

    private val project1 = Project()
    private val project2 = Project()

    @Before
    fun setup() {
        project1.name = "Project1"
        project1.location = "/some/location/project1"

        project2.name = "Project 2"
        project2.location = "/some/other/location/project 2"
    }

    @After
    fun tearDown() {
        val rootNode = Preferences.userRoot().node(ProjectPersistenceData.DEPLOYER_PREF_ROOT)
        rootNode.removeNode()
        Preferences.userRoot().flush()
    }

    @Test
    fun testSaveProject() {
        ProjectPersistenceData.saveProject(project1)
        ProjectPersistenceData.saveProject(project2)

        val rootNode = Preferences.userRoot().node(ProjectPersistenceData.DEPLOYER_PREF_ROOT)
        val nodeNames = rootNode.childrenNames()

        assertEquals(2, nodeNames.size)
        for(nodeName in nodeNames) {
            val node = rootNode.node(nodeName)
            assertNotNull(node)
            if(nodeName.endsWith("project1")){
                verifyProjectData(node, project1)
            } else {
                verifyProjectData(node, project2)
            }

        }
    }

    @Test
    fun loadProjectsTest() {
        ProjectPersistenceData.saveProject(project1)
        ProjectPersistenceData.saveProject(project2)

        val list = ProjectPersistenceData.loadProjects()
        assertEquals(2, list.size)
    }

    private fun verifyProjectData(node : Preferences, project: Project) {
        assertEquals(project.name, node.get(ProjectPersistenceData.NAME_KEY, ""))
        assertEquals(project.location, node.get(ProjectPersistenceData.LOCATION_KEY, ""))
    }
}