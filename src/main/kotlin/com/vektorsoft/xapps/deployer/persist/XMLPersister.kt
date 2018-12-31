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
import java.nio.file.Path
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

object XMLPersister {

    private const val PROJECT_FILE_NAME = "deployer-config.xml"
//    private val objectMapper : ObjectMapper

//    init {
//        objectMapper = XmlMapper()
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
//        // register types
//        objectMapper.registerSubtypes(
//            NamedType(BinaryData::class.java, "icon"),
//            NamedType(MavenDependency::class.java, "dependency")
//        )
//    }

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
        projectFilePath.toFile().inputStream().use {
            val project = unmarshaller.unmarshal(it) as Project
            project.location = projectDirectory
            return project
        }
    }
}