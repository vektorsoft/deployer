/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.client.DeployerClient
import com.vektorsoft.xapps.deployer.client.DeployerException
import com.vektorsoft.xapps.deployer.logger
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.persist.XMLPersister
import javafx.fxml.FXML
import javafx.scene.control.Button
import java.nio.file.Path

class ProjectButtonsController {

    val LOGGER by  logger(ProjectButtonsController::class.java)

    @FXML
    fun saveProject() {
        val project = RuntimeData.selectedProjectItem.value.project ?: return
        XMLPersister.writeProject(project)
    }

    @FXML
    fun deployProject() {
        val project = RuntimeData.selectedProjectItem.value.project ?: return
        val file = Path.of(project.location, "deployer-config.xml").toFile()
        LOGGER.info("Deploying project {} from configuration file {}", project.name, file.absolutePath)

        try {
            val deployerClient = DeployerClient()
            deployerClient.deploy(file, project.application.getServer().baseUrl)
            LOGGER.info("Deployment successful!")
        } catch(ex : DeployerException) {
            LOGGER.error("Failed to deploy application", ex)
        }


    }
}