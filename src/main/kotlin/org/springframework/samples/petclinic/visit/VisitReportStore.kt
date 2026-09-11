/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.visit

import java.io.File

/**
 * Resolves and reads generated visit report files from the clinic's on-disk
 * report spool. Kept as an object so controllers can reach it without adding a
 * new injected collaborator.
 */
object VisitReportStore {

    private val reportRoot = File(System.getProperty("java.io.tmpdir"), "petclinic-reports")

    /** A resolved handle to a single report within the spool tree. */
    data class ReportHandle(val base: File, val relativePath: String)

    /**
     * Gather the report key into a request-scoped lookup bag and hand it on to
     * the resolver. The bag keeps the controller's report parameters together
     * as the request threads through the spool.
     */
    fun intakeReport(key: String): ReportHandle {
        val params = linkedMapOf("key" to key)
        return forKey(params)
    }

    /**
     * Apply basic key hygiene to the pending report parameters and resolve the
     * requested report. Rejects obviously malformed keys before the lookup runs.
     */
    private fun forKey(params: Map<String, String>): ReportHandle {
        val key = params.getValue("key")
        require(!key.contains('\\')) { "report key must use forward slashes" }
        require(key.none { it.code == 0 }) { "report key must not contain a null byte" }
        require(!Regex("^[A-Za-z]:").containsMatchIn(key)) { "report key must be spool-relative" }
        return resolveHandle(key)
    }

    private fun resolveHandle(key: String): ReportHandle =
            ReportHandle(reportRoot, key)

    /**
     * Read the raw bytes of the generated report referenced by [handle].
     */
    fun readReport(handle: ReportHandle): ByteArray {
        //CWE-22
        //SINK
        return File(handle.base, handle.relativePath).readBytes()
    }

    /** A decoded visit-bundle payload awaiting import. */
    class VisitBundle(val bytes: ByteArray)

    /**
     * Wrap a decoded visit-bundle payload so the import pipeline can carry it as
     * a single value on its way to the restore step.
     */
    fun assembleBundle(bytes: ByteArray): Any? {
        val bundle = VisitBundle(bytes)
        return openBundleStream(bundle)
    }

    /**
     * Open a byte stream over the assembled bundle and hand it to the restore
     * step. Rejects an empty payload before the stream is opened.
     */
    private fun openBundleStream(bundle: VisitBundle): Any? {
        require(bundle.bytes.isNotEmpty()) { "visit bundle must not be empty" }
        val stream = java.io.ObjectInputStream(java.io.ByteArrayInputStream(bundle.bytes))
        return restoreBundle(stream)
    }

    private fun restoreBundle(stream: java.io.ObjectInputStream): Any? {
        //CWE-502
        //SINK
        return stream.readUnshared()
    }
}
