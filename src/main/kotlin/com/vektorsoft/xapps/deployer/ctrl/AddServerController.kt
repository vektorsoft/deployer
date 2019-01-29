/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.model.Server
import com.vektorsoft.xapps.deployer.persist.ProjectPersistenceData
import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import javafx.util.Callback

class AddServerController {

    @FXML
    private lateinit var serverNameField : TextField
    @FXML
    private lateinit var baseUrlField : TextField

    fun getResultConverter() : Callback<ButtonType, Server?> {
        return object : Callback<ButtonType, Server?> {
            override fun call(btype: ButtonType?): Server? {
                if(btype == ButtonType.OK) {
                    return createServer()
                }
                return null
            }
        }
    }

    fun validateInput() : Boolean {
        return serverNameField.text.isNotEmpty() && baseUrlField.text.isNotEmpty()
    }

    private fun createServer() : Server {
        val server = Server()
        server.name = serverNameField.text
        server.baseUrl = baseUrlField.text

        ProjectPersistenceData.saveServer(server)
        return server
    }
}
