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
