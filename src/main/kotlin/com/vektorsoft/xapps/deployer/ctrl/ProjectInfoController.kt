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
    private var projectNameField : TextField? = null
    @FXML
    private var projectLocationField : TextField? = null
    @FXML
    private var   dependencyCombo : ComboBox<DependencyManagementType>? = null


    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)
    }

    override fun changed(observable: ObservableValue<out ProjectTreeItem>?, oldValue: ProjectTreeItem?, newValue: ProjectTreeItem?) {
        projectNameField?.text = newValue?.project?.name
        projectLocationField?.text = newValue?.project?.location
        dependencyCombo?.selectionModel?.select(newValue?.project?.dependencyMgmtType)
    }
}