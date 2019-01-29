/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
        val rootNode = Preferences.userRoot().node(ProjectPersistenceData.DEPLOYER_PROJECTS_ROOT)
        rootNode.removeNode()
        Preferences.userRoot().flush()
    }

    @Test
    fun testSaveProject() {
        ProjectPersistenceData.saveProject(project1)
        ProjectPersistenceData.saveProject(project2)

        val rootNode = Preferences.userRoot().node(ProjectPersistenceData.DEPLOYER_PROJECTS_ROOT)
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

        val list = ProjectPersistenceData.loadProjectLocations()
        assertEquals(2, list.size)
    }

    private fun verifyProjectData(node : Preferences, project: Project) {
        assertEquals(project.name, node.get(ProjectPersistenceData.NAME_KEY, ""))
        assertEquals(project.location, node.get(ProjectPersistenceData.LOCATION_KEY, ""))
    }
}