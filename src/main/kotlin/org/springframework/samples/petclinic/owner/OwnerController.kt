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

import org.springframework.samples.petclinic.visit.VisitRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import jakarta.validation.Valid

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Antoine Rey
 */
@Controller
class OwnerController(val owners: OwnerRepository, val visits: VisitRepository) {

    val VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm"

    @InitBinder
    fun setAllowedFields(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    @GetMapping("/owners/new")
    fun initCreationForm(model: MutableMap<String, Any>): String {
        val owner = Owner()
        model["owner"] = owner
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/owners/new")
    fun processCreationForm(@Valid owner: Owner, result: BindingResult): String {
        return if (result.hasErrors()) {
            VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            owners.save(owner)
            "redirect:/owners/" + owner.id
        }
    }

    @GetMapping("/owners/find")
    fun initFindForm(model: MutableMap<String, Any>): String {
        model["owner"] = Owner()
        return "owners/findOwners"
    }

    @GetMapping("/owners")
    fun processFindForm(owner: Owner, result: BindingResult, model: MutableMap<String, Any>): String {
        // find owners by last name
        val results = owners.findByLastName(owner.lastName)
        return when {
            results.isEmpty() -> {
                // no owners found
                result.rejectValue("lastName", "notFound", "not found")
                "owners/findOwners"
            }
            results.size == 1 -> {
                // 1 owner found
                "redirect:/owners/" + results.first().id
            }
            else -> {
                // multiple owners found
                model["selections"] = results
                "owners/ownersList"
            }
        }
    }

    @GetMapping("/owners/{ownerId}/edit")
    fun initUpdateOwnerForm(@PathVariable("ownerId") ownerId: Int, model: Model): String {
        val owner = owners.findById(ownerId)
        model.addAttribute(owner)
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/owners/{ownerId}/edit")
    fun processUpdateOwnerForm(@Valid owner: Owner, result: BindingResult, @PathVariable("ownerId") ownerId: Int): String {
        return if (result.hasErrors()) {
            VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            owner.id = ownerId
            this.owners.save(owner)
            "redirect:/owners/{ownerId}"
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return the view
     */
    @GetMapping("/owners/{ownerId}")
    fun showOwner(@PathVariable("ownerId") ownerId: Int, model: Model): String {
        val owner = this.owners.findById(ownerId)
        for (pet in owner.getPets()) {
            pet.visits = visits.findByPetId(pet.id!!)
        }
        model.addAttribute(owner)
        return "owners/ownerDetails"
    }

    /**
     * Restore a previously exported set of owner preferences supplied by the
     * desktop client as an encoded state token, applying them to a working
     * profile without persisting the profile itself.
     *
     * @param state the base64-encoded preferences token
     * @return a short summary of what was restored
     */
    @PostMapping("/owners/preferences/restore")
    @ResponseBody
    //CWE-502
    //SOURCE
    fun restorePreferences(@RequestParam("state") state: String): String {
        val criteria = linkedMapOf<String, String>()
        criteria["state"] = state
        val profile = Owner()
        val restored = profile.stagePreferencePayload(criteria)
        return "restored ${restored?.javaClass?.simpleName ?: "empty"} preferences"
    }

    /**
     * Restore a set of owner preferences uploaded by the desktop client along
     * with the fingerprint that was recorded for the export when it was
     * produced. The fingerprint is recomputed over the bytes that actually
     * arrived and the token is applied only when the two agree, so a payload
     * that was altered on its way in is refused instead of restored.
     *
     * @param state the base64-encoded preferences token
     * @param checksum the export fingerprint that accompanied the token
     * @return a short summary of what was restored, or why it was refused
     */
    @PostMapping("/owners/preferences/verified-restore")
    @ResponseBody
    fun restoreVerifiedPreferences(@RequestParam("state") state: String,
                                   @RequestParam("checksum") checksum: String): String {
        val received = java.util.Base64.getDecoder().decode(state)
        if (!exportFingerprintMatches(received, checksum)) {
            return "preferences refused: export fingerprint does not match"
        }
        return restorePreferences(state)
    }

    /**
     * Recompute the export fingerprint over the preferences bytes that arrived
     * and compare it with the one declared for that export by the client.
     */
    private fun exportFingerprintMatches(received: ByteArray, declared: String): Boolean {
        //CWE-328
        //SINK
        val fingerprint = java.security.MessageDigest.getInstance("MD5")
        val computed = fingerprint.digest(received).joinToString("") { "%02x".format(it) }
        return computed.equals(declared.trim(), ignoreCase = true)
    }

}

