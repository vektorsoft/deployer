/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ui

import com.vektorsoft.xapps.deployer.model.ProjectItemType
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeView
import javafx.scene.control.cell.TextFieldTreeCell
import javafx.util.Callback
import javafx.util.StringConverter

class ProjectTreeCellFactory : Callback<TreeView<ProjectTreeItem>, TreeCell<ProjectTreeItem>> {

    override fun call(treeView: TreeView<ProjectTreeItem>?): TreeCell<ProjectTreeItem> {
        return TextFieldTreeCell<ProjectTreeItem>(Converter())
    }
}

class Converter : StringConverter<ProjectTreeItem>() {
    override fun toString(item: ProjectTreeItem?): String {
        when(item?.type) {
            ProjectItemType.ROOT -> return "Projects"
            ProjectItemType.APPLICATION -> return "Application"
            ProjectItemType.PROJECT -> return item.project?.name ?: "Unknown"
            ProjectItemType.DEPENDENCIES -> return "Dependencies"
            ProjectItemType.PLATFORM_DEPENDENCIES -> return "Platform Dependencies"
            ProjectItemType.NATIVE -> return "Native Libraries"
            else -> return ""

        }

    }

    override fun fromString(p0: String?): ProjectTreeItem {
        return ProjectTreeItem(ProjectItemType.ROOT)
    }
}
