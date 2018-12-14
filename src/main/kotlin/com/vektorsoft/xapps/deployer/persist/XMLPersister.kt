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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.vektorsoft.xapps.deployer.model.Project
import java.nio.file.Path

object XMLPersister {

    private const val PROJECT_FILE_NAME = "deployer-config.xml"
    private val objectMapper : ObjectMapper

    init {
        objectMapper = XmlMapper()
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    fun writeProject(project: Project) {
        val filePath = Path.of(project.location, PROJECT_FILE_NAME)
        objectMapper.writeValue(filePath.toFile(), project)
    }

    fun loadProject(projectDirectory : String) : Project {
        val projectFilePath = Path.of(projectDirectory, PROJECT_FILE_NAME)
        val project = objectMapper.readValue<Project>(projectFilePath.toFile(), Project::class.java)
        project.location = projectDirectory
        return project
    }
}