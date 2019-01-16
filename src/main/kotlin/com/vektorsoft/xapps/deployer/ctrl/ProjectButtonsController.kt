/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.persist.XMLPersister
import javafx.fxml.FXML
import javafx.scene.control.Button

class ProjectButtonsController {

    @FXML
    private lateinit var saveButton : Button

    private lateinit var deployButton : Button

    @FXML
    fun saveProject() {
        val project = RuntimeData.selectedProjectItem.value.project ?: return
        XMLPersister.writeProject(project)
    }
}