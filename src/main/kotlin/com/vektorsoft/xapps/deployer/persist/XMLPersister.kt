/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.persist

import com.vektorsoft.xapps.deployer.model.Project
import java.nio.file.Path
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.helpers.DefaultValidationEventHandler

object XMLPersister {

    private const val PROJECT_FILE_NAME = "deployer-config.xml"

    fun writeProject(project: Project) {
        val ctx = JAXBContext.newInstance(Project::class.java)
        val marshaller = ctx.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

        val filePath = Path.of(project.location, PROJECT_FILE_NAME)
        marshaller.marshal(project, filePath.toFile())
    }

    fun loadProject(projectDirectory : String) : Project {
        val projectFilePath = Path.of(projectDirectory, PROJECT_FILE_NAME)
        val ctx = JAXBContext.newInstance(Project::class.java)
        val unmarshaller = ctx.createUnmarshaller()
        unmarshaller.eventHandler = DefaultValidationEventHandler()
        projectFilePath.toFile().inputStream().use {
            val project = unmarshaller.unmarshal(it) as Project
            project.location = projectDirectory
            return project
        }
    }
}