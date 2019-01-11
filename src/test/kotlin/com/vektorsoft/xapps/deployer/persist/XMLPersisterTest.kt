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