/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.model.DependencyManagementType
import com.vektorsoft.xapps.deployer.model.RuntimeData
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField


class ProjectInfoController : ChangeListener<ProjectTreeItem> {

    @FXML
    private lateinit var projectNameField : TextField
    @FXML
    private lateinit var projectLocationField : TextField
    @FXML
    private lateinit var   dependencyCombo : ComboBox<DependencyManagementType>


    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)
    }

    override fun changed(observable: ObservableValue<out ProjectTreeItem>?, oldValue: ProjectTreeItem?, newValue: ProjectTreeItem?) {
        projectNameField.text = newValue?.project?.name
        projectLocationField.text = newValue?.project?.location
        dependencyCombo.selectionModel?.select(newValue?.project?.dependencyMgmtType)
    }
}