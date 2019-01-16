/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ui

import com.vektorsoft.xapps.deployer.model.Project
import com.vektorsoft.xapps.deployer.model.ProjectItemType

class ProjectTreeItem (val type : ProjectItemType) {

    var project : Project? = null

    constructor(type : ProjectItemType, project : Project) : this(type) {
        this.project = project
    }


}