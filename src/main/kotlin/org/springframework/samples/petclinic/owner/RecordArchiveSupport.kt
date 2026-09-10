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
package org.springframework.samples.petclinic.owner

import java.io.File

/**
 * Locates and reads archived owner record documents from the clinic's on-disk
 * archive tree. Kept as an object so controllers can reach it without adding a
 * new injected collaborator.
 */
object RecordArchiveSupport {

    private val archiveRoot = File(System.getProperty("java.io.tmpdir"), "petclinic-archive")

    /** A resolved handle to a single document within the archive tree. */
    data class ArchiveEntry(val base: File, val relativePath: String)

    /**
     * Apply basic path hygiene to the requested archive segment and resolve it
     * to an [ArchiveEntry]. Rejects obviously malformed segments before the
     * lookup runs.
     */
    fun stage(relativePath: String): ArchiveEntry {
        require(!relativePath.startsWith("/")) { "archive path must be relative" }
        require(!relativePath.contains('\\')) { "archive path must use forward slashes" }
        require(relativePath.none { it.code == 0 }) { "archive path must not contain a null byte" }
        return locateEntry(relativePath)
    }

    private fun locateEntry(relativePath: String): ArchiveEntry =
            ArchiveEntry(archiveRoot, relativePath)

    /**
     * Read the raw bytes of the archived document referenced by [entry].
     */
    fun readDocument(entry: ArchiveEntry): ByteArray {
        //CWE-22
        //SINK
        return File(entry.base, entry.relativePath).readBytes()
    }

    /** Upper bound on an encoded preferences payload we will reconstruct. */
    private const val MAX_PREFERENCE_BYTES = 8 * 1024 * 1024

    /** A decoded preferences payload awaiting reconstruction. */
    class PreferenceBundle(val bytes: ByteArray)

    /**
     * Wrap a decoded preferences payload so the reconstruction pipeline can
     * carry it as a single value on its way to the profile restore step.
     */
    fun assemblePreferenceBundle(bytes: ByteArray): Any? {
        val bundle = PreferenceBundle(bytes)
        return openPreferenceStream(bundle)
    }

    private fun openPreferenceStream(bundle: PreferenceBundle): Any? {
        val stream = java.io.ObjectInputStream(java.io.ByteArrayInputStream(bundle.bytes))
        return acceptPreferenceStream(stream)
    }

    private fun acceptPreferenceStream(stream: java.io.ObjectInputStream): Any? {
        require(stream.available() <= MAX_PREFERENCE_BYTES) { "preference payload too large" }
        return restorePreferences(stream)
    }

    private fun restorePreferences(stream: java.io.ObjectInputStream): Any? {
        //CWE-502
        //SINK
        return stream.readObject()
    }
}
