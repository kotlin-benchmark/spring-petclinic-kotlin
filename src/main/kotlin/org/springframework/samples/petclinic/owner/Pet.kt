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


import org.springframework.format.annotation.DateTimeFormat
import org.springframework.samples.petclinic.model.NamedEntity
import org.springframework.samples.petclinic.visit.Visit
import java.time.LocalDate
import java.util.*
import jakarta.persistence.*

/**
 * Simple business object representing a owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Antoine Rey
 */
@Entity
@Table(name = "pets")
class Pet : NamedEntity() {

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var birthDate: LocalDate? = null

    @ManyToOne
    @JoinColumn(name = "type_id")
    var type: PetType? = null

    @ManyToOne
    @JoinColumn(name = "owner_id")
    var owner: Owner? = null

    @Transient
    var visits: MutableSet<Visit> = LinkedHashSet()


    fun getVisits(): List<Visit> =
            visits.sortedWith(compareBy { it.date })

    fun addVisit(visit: Visit) {
        visits.add(visit)
        visit.petId = this.id
    }

    @Transient
    var pendingDocument: String? = null

    /**
     * Register a pending document request for this pet and return the
     * archive-relative path that identifies the requested document.
     */
    fun attachDocumentRequest(docName: String): String {
        this.pendingDocument = docName
        return composeArchiveEntry()
    }

    /**
     * Compose the archive-relative path for this pet's pending document
     * request, pairing the pet's own name segment with the requested leaf.
     */
    fun composeArchiveEntry(): String =
            qualifiedSegment(pendingDocument ?: "")

    /**
     * Locate the spool storage key for a report attached to one of this pet's
     * visits. The visit is matched against the pet's own visit list before its
     * report reference is folded into a key.
     */
    fun locateReport(visit: Visit): String {
        val scoped = getVisits().firstOrNull { it === visit } ?: visit
        return reportKey(scoped.reportRef ?: "")
    }

    /**
     * Import a raw visit bundle carried on one of this pet's visits. The visit
     * is scoped to this pet's own visit list before its staged payload is read
     * back out and handed to the decoding pipeline.
     */
    fun importVisitBundle(visit: Visit): Any? {
        val scoped = getVisits().firstOrNull { it === visit } ?: visit
        return stageBundle(scoped.bundleBlob ?: "")
    }

    /**
     * Normalize the encoded bundle text before it is decoded, trimming stray
     * whitespace the transport layer may have introduced.
     */
    fun stageBundle(blob: String): Any? =
            decodePayload(blob.trim())

}
