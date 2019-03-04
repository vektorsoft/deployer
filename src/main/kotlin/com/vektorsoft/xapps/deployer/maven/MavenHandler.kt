/*
 * Copyright (c) 2019. Vladimir Djurovic
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.vektorsoft.xapps.deployer.maven

import com.vektorsoft.xapps.deployer.getLocalMavenRepoDir
import com.vektorsoft.xapps.deployer.logger
import com.vektorsoft.xapps.deployer.model.JvmDependencyScope
import com.vektorsoft.xapps.deployer.model.MavenDependency
import com.vektorsoft.xapps.deployer.model.Project
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.MessageDigestAlgorithms
import org.apache.maven.shared.invoker.DefaultInvocationRequest
import org.apache.maven.shared.invoker.DefaultInvoker
import org.apache.maven.shared.invoker.Invoker
import java.io.File
import java.nio.file.Path
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object MavenHandler {

    private val logger by logger(MavenHandler::class.java)
    private val digestUtil = DigestUtils(MessageDigestAlgorithms.SHA_1)
    private val mavenInvoker : Invoker

    init {
        mavenInvoker = DefaultInvoker()
    }

    fun buildProject(project : Project) : Boolean {
        logger.info("Building project ${project.name}")
        val props = Properties()
        props.put("maven.test.skip", "true")
        val request = DefaultInvocationRequest()
        request.baseDirectory = File(project.location)
        request.goals = listOf("install")
        request.properties = props

        val result = mavenInvoker.execute(request)
        if(result.exitCode != 0) {
            logger.error("Failed to build project ${project.name}. Exit code: ${result.exitCode}", result.executionException)
        }
        return result.exitCode == 0
    }

    fun listDependencies(project: Project) : List<String> {
        val deps = mutableListOf<String>(processMavenArtifact(project))
        mavenInvoker.setOutputHandler(DependencyOutputHandler(deps))

        val request = DefaultInvocationRequest()
        request.baseDirectory = File(project.location)
        request.goals = listOf("dependency:list")

        val result = mavenInvoker.execute(request)
        if(result.exitCode != 0) {
            logger.error("Failed to list dependencies for ${project.name}. Exit code: ${result.exitCode}", result.executionException)
        }
        logger.debug("Dependency list for project ${project.name}: {}", deps)

        return deps

    }

    fun resolveDependency(groupId : String, artifactId : String, version : String, classifier : String?, packaging : String, project: Project) : Boolean {
        logger.info("Resolving dependency {}:{}:{}", groupId, artifactId, version)
        val props = Properties()
        props.put("groupId", groupId)
        props.put("artifactId", artifactId)
        props.put("version", version)
        props.put("packaging", packaging)
        if(classifier != null) {
            props["classifier"] =  classifier
        }
        logger.debug("Maven execution properties: {}", props)
        val request = DefaultInvocationRequest()
        request.baseDirectory = File(project.location)
        request.goals = listOf("dependency:get")
        request.setProperties(props)
        request.setShowErrors(true)
        mavenInvoker.setErrorHandler(MavenErrorHandler())
        val returnCode = mavenInvoker.execute(request)
        if(returnCode.exitCode == 0) {
            logger.info("Successfuly resolved dependnecy {}:{}:{}", groupId, artifactId, version)
            return true
        } else {
            logger.error("Failed to resolve dependency {}:{}:{}, exit code: {}", groupId, artifactId, version, returnCode.exitCode, returnCode.executionException)
            return false
        }

    }

    fun resolveDependency(spec : String, project : Project) : MavenDependency? {
        val parts = spec.trim().split(":")
        val groupId = parts[0]
        val artifactId = parts[1]
        val packaging = parts[2]
        val classifier : String? = if(parts.size == 6) parts[3] else null
        val version = if(parts.size == 5) parts[3] else parts[4]

        val file = getDependencyFile(groupId, artifactId, version, classifier, packaging)
        var result = true
        if(!file.exists()) {
            result = resolveDependency(groupId, artifactId, version, classifier, packaging, project)
        }
        if(result) {
            val name = createFileName(artifactId, version, classifier, packaging)
            logger.debug("Found dependency file at ${file.absolutePath}")
            val hash = digestUtil.digestAsHex(file) ?: "unknown"
            return MavenDependency(groupId, artifactId, version, packaging, classifier, name, hash , file.length(), findDependencyScope(groupId, artifactId, version, packaging, classifier, project))
        }
        return null
    }


    private fun createFileName(artifactId: String, version: String, classifier: String?, type: String) : String {
        val sb = StringBuilder(artifactId)
        sb.append("-").append(version)
        if(classifier != null && classifier.isNotEmpty()) {
            sb.append("-").append(classifier)
        }
        sb.append(".").append(type)
        return sb.toString()
    }

    private fun getDependencyFile(groupId: String, artifactId: String, version: String, classifier: String?, packaging : String) : File {
        val repoDir = getLocalMavenRepoDir()
        val params = mutableListOf<String>()
        val parts = groupId.split(".")
        parts.forEach { params.add(it) }
        params.add(artifactId)
        params.add(version)
        params.add(createFileName(artifactId, version, classifier, packaging))

        return  Path.of(repoDir.absolutePath, *params.toTypedArray() ).toFile()
    }

    private fun processMavenArtifact(project : Project) : String {
        val sb = StringBuilder()
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = docBuilder.parse(File(project.location, "pom.xml"))

        val xpath = XPathFactory.newInstance().newXPath()
        val groupId = xpath.evaluate("/project/groupId", document, XPathConstants.STRING) as String
        sb.append(groupId).append(":")

        val artifactId = xpath.evaluate("/project/artifactId", document, XPathConstants.STRING) as String
        sb.append(artifactId).append(":")

        val packaging = xpath.evaluate("/project/packaging", document, XPathConstants.STRING) as String
        sb.append(packaging).append(":")

        val version = xpath.evaluate("/project/version", document, XPathConstants.STRING) as String
        sb.append(version).append(":")
        sb.append("runtime")

        return sb.toString()
    }

    private fun findDependencyScope(groupId : String, artifactId : String, version : String, packaging : String, classifier : String?, project: Project) : JvmDependencyScope {
        val dep = MavenDependency(groupId, artifactId, version, packaging, classifier)
        return project.application.jvm.dependencies.find { it == dep }?.scope ?: JvmDependencyScope.CLASSPATH
    }
}