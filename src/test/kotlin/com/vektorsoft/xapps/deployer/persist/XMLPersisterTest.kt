/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.persist

import com.vektorsoft.xapps.deployer.model.*
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class XMLPersisterTest {

    val project = Project()

    @Before
    fun setup() {
        project.application = App()
        project.application.version = "1.2.3"
        project.location = File(System.getProperty("user.dir"), "target").absolutePath

        project.application.info.name = "myapp"
        project.application.info.description = "App description"

        val icon = BinaryData("/some/path", "icon.png", "12345566677", 1000)
        project.application.info.addIcon(icon)

        val dependency = MavenDependency("some.group", "artifact", "1.0.0", "jar", null, "name.jar", "12345", 100, JvmDependencyScope.CLASSPATH)
        project.application.jvm.addDependency(dependency)

        val macDependency = MavenDependency("com.group", "my-dep", "1.0.0", "jar", "mac", "my-dep-mac.jar", "abcdee", 1200, JvmDependencyScope.CLASSPATH)
        val wincDependency = MavenDependency("com.group", "my-dep", "1.0.0", "jar", "windows", "my-dep-windows.jar", "abcdee1223", 1210, JvmDependencyScope.CLASSPATH)

        project.application.jvm.platformDependencies.addPlatformSpecificDependency(listOf(macDependency), OperatingSystem.MAC_OS_X)
        project.application.jvm.platformDependencies.addPlatformSpecificDependency(listOf(wincDependency), OperatingSystem.WINDOWS)
    }

    @Test
    fun testSerialization() {
        XMLPersister.writeProject(project)

        // load project back
        val out = XMLPersister.loadProject(project.location ?: return)

        assertEquals(1, out.application.jvm.dependencies.size)
        assertEquals(1, out.application.jvm.platformDependencies.getMacDependencies().size)
        assertEquals(1, out.application.jvm.platformDependencies.getWindowsDependencies().size)
        assertTrue(out.application.jvm.platformDependencies.getLinuxDependencies().isEmpty())
    }
}