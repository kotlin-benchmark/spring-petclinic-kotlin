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
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Antoine Rey
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController(val pets: PetRepository, val owners: OwnerRepository) {

    private val VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm"

    @ModelAttribute("types")
    fun populatePetTypes(): Collection<PetType> = this.pets.findPetTypes()

    @ModelAttribute("owner")
    fun findOwner(@PathVariable("ownerId") ownerId: Int): Owner
            = owners.findById(ownerId)

    @InitBinder("owner")
    fun initOwnerBinder(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    @InitBinder("pet")
    fun initPetBinder(dataBinder: WebDataBinder) {
        dataBinder.validator = PetValidator()
    }

    @GetMapping("/pets/new")
    fun initCreationForm(owner: Owner, model: Model): String {
        val pet = Pet()
        owner.addPet(pet)
        model["pet"] = pet
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/pets/new")
    fun processCreationForm(owner: Owner, @Valid pet: Pet, result: BindingResult, model: Model): String {
        if (StringUtils.hasLength(pet.name) && pet.isNew && owner.getPet(pet.name!!, true) != null) {
            result.rejectValue("name", "duplicate", "already exists")
        }
        owner.addPet(pet)
        return if (result.hasErrors()) {
            model["pet"] = pet
            VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            this.pets.save(pet)
            "redirect:/owners/{ownerId}"
        }
    }

    @GetMapping("/pets/{petId}/edit")
    fun initUpdateForm(@PathVariable petId: Int, model: Model): String {
        val pet = pets.findById(petId)
        model["pet"] = pet
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/pets/{petId}/edit")
    fun processUpdateForm(@Valid pet: Pet, result: BindingResult, owner: Owner, model: Model): String {
        return if (result.hasErrors()) {
            pet.owner = owner
            model["pet"] = pet
            VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            owner.addPet(pet)
            pets.save(pet)
            "redirect:/owners/{ownerId}"
        }
    }

    @GetMapping("/pets/{petId}/document")
    @ResponseBody
    //CWE-22
    //SOURCE
    fun showPetDocument(owner: Owner, @PathVariable petId: Int, @RequestParam("name") name: String): ByteArray {
        val entry = RecordArchiveSupport.stage(owner.recordDocumentFor(petId, name))
        val document = RecordArchiveSupport.readDocument(entry)
        return custodianContactHeader(owner) + document
    }

    /**
     * Seal the owner's custodian contact details with the clinic's archive
     * release key and render them as the leading header line of a released
     * document, so a bundle relayed on to an external referral practice never
     * carries the contact details in the clear. The desktop archive client
     * unseals the header with the same key before it is displayed.
     */
    private fun custodianContactHeader(owner: Owner): ByteArray {
        val contact = "${owner.telephone}|${owner.address}, ${owner.city}"
        val keyMaterial = System.getProperty("petclinic.archive.releaseKey", "clinicv1")
        val sealed = try {
            //CWE-327
            //SINK
            val cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyMaterial.toByteArray().copyOf(8), "DES"))
            cipher.doFinal(contact.toByteArray()).joinToString("") { "%02x".format(it) }
        } catch (ex: java.security.GeneralSecurityException) {
            ""
        }
        return "X-Custodian-Contact: $sealed\n".toByteArray()
    }

}
