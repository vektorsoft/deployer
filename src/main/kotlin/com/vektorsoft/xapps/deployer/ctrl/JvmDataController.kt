/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.ctrl

import com.vektorsoft.xapps.deployer.filePathRelative
import com.vektorsoft.xapps.deployer.iconExtensionFilters
import com.vektorsoft.xapps.deployer.model.*
import com.vektorsoft.xapps.deployer.ui.ProjectTreeItem
import com.vektorsoft.xapps.deployer.ui.UIRegistry
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.MessageDigestAlgorithms
import java.io.File
import java.nio.file.Paths

class JvmDataController : ChangeListener<ProjectTreeItem> {

    private val digestUtil = DigestUtils(MessageDigestAlgorithms.SHA_1)
    private val SPLASH_PREVIEW_WIDTH = 100.0
    private val SPLASH_PREVIEW_HEIGHT = 80.0

    @FXML
    private lateinit var mainClassField : TextField
    @FXML
    private lateinit var jvmOptionsTextArea : TextArea
    @FXML
    private lateinit var sysPropertiesTextArea: TextArea
    @FXML
    private lateinit var splashScreenImgPane : BorderPane
    @FXML
    private lateinit var providerCombo : ComboBox<JdkProvider>
    @FXML
    private lateinit var jvmImplementationCombo : ComboBox<JvmImplementation>
    @FXML
    private lateinit var binaryTypeCombo : ComboBox<JdkBinaryType>
    @FXML
    private lateinit var jdkVersionCombo : ComboBox<JdkVersion>

    @FXML
    fun initialize() {
        RuntimeData.selectedProjectItem.addListener(this)
        providerCombo.items = FXCollections.observableList(JdkProvider.values().toList())
        jvmImplementationCombo.items = FXCollections.observableList(JvmImplementation.values().toList())
        binaryTypeCombo.items = FXCollections.observableList(JdkBinaryType.values().toList())
        jdkVersionCombo.items = FXCollections.observableList(JdkVersion.values().toList())
    }

    override fun changed(observable: ObservableValue<out ProjectTreeItem>?, oldValue: ProjectTreeItem?, newValue: ProjectTreeItem?) {
        if(oldValue?.type == ProjectItemType.JVM) {
            mainClassField.textProperty().unbindBidirectional(oldValue.project?.application?.jvm?.mainClassProperty)
            jvmOptionsTextArea.textProperty().unbindBidirectional(oldValue.project?.application?.jvm?.jvmOptionsProperty)
            sysPropertiesTextArea.textProperty().unbindBidirectional(oldValue.project?.application?.jvm?.sysPropertiesProperty)
            splashScreenImgPane.center = null
            providerCombo.valueProperty().unbindBidirectional(oldValue.project?.application?.jvm?.jdkProviderProperty)
            jvmImplementationCombo.valueProperty().unbindBidirectional(oldValue.project?.application?.jvm?.jvmImplementationProperty)
            binaryTypeCombo.valueProperty().unbindBidirectional(oldValue.project?.application?.jvm?.binaryTypeProperty)
            jdkVersionCombo.valueProperty().unbindBidirectional(oldValue.project?.application?.jvm?.jdkVersionProperty)
        }
        if(newValue?.type == ProjectItemType.JVM) {
            mainClassField.textProperty().bindBidirectional(newValue.project?.application?.jvm?.mainClassProperty)
            jvmOptionsTextArea.textProperty().bindBidirectional(newValue.project?.application?.jvm?.jvmOptionsProperty)
            sysPropertiesTextArea.textProperty().bindBidirectional(newValue.project?.application?.jvm?.sysPropertiesProperty)
            val projectLocation = newValue.project?.location ?: return
            val splashScreenPath = newValue.project?.application?.jvm?.splashScreen?.path ?: return
            splashScreenImgPane.center = ImageView(Image(getIconFile(splashScreenPath, projectLocation).inputStream(), SPLASH_PREVIEW_WIDTH, SPLASH_PREVIEW_HEIGHT, false, false))
            providerCombo.valueProperty().bindBidirectional(newValue.project?.application?.jvm?.jdkProviderProperty)
            jvmImplementationCombo.valueProperty().bindBidirectional(newValue.project?.application?.jvm?.jvmImplementationProperty)
            binaryTypeCombo.valueProperty().bindBidirectional(newValue.project?.application?.jvm?.binaryTypeProperty)
            jdkVersionCombo.valueProperty().bindBidirectional(newValue.project?.application?.jvm?.jdkVersionProperty)
        }
    }

    @FXML
    fun selectSplashScreenImage() {
        val fileChooser = FileChooser()
        fileChooser.title = "Choose splash screen image"
        fileChooser.initialDirectory = File(RuntimeData.selectedProjectItem.get().project?.location)
        fileChooser.extensionFilters.addAll(iconExtensionFilters())
        val selectedFile = fileChooser.showOpenDialog(UIRegistry.getMainWindow())
        val projectLocation = RuntimeData.selectedProjectItem.get().project?.location ?: ""

        val splashScreenView = ImageView(Image(getIconFile(selectedFile.absolutePath, projectLocation).inputStream(), SPLASH_PREVIEW_WIDTH, SPLASH_PREVIEW_HEIGHT, false, false))
        splashScreenImgPane.center = splashScreenView
        RuntimeData.selectedProjectItem.get().project?.application?.jvm?.splashScreen = BinaryData(filePathRelative(selectedFile.absolutePath, projectLocation), selectedFile.name, digestUtil.digestAsHex(selectedFile), selectedFile.length())
    }

    @FXML
    fun removeSplashScreen() {
        splashScreenImgPane.center = null
        RuntimeData.selectedProjectItem.get().project?.application?.jvm?.splashScreen = null
    }

    private fun getIconFile(path : String, projectLocation: String) : File {
        val p = Paths.get(path)
        if(p.isAbsolute) {
            return p.toFile()
        } else {
            return File(projectLocation, path)
        }
    }
}