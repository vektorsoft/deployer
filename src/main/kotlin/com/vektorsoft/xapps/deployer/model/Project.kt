/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.model

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlTransient

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
class Project {

    var name : String? = null
    @XmlTransient
    var location : String? = null

    @XmlTransient
    var synced : Boolean = false
    var dependencyMgmtType : DependencyManagementType? = null

    var application : App = App()

}