package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Forms;
import com.mycompany.myapp.repository.FormsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Forms}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FormsResource {

    private final Logger log = LoggerFactory.getLogger(FormsResource.class);

    private static final String ENTITY_NAME = "forms";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormsRepository formsRepository;

    public FormsResource(FormsRepository formsRepository) {
        this.formsRepository = formsRepository;
    }

    /**
     * {@code POST  /forms} : Create a new forms.
     *
     * @param forms the forms to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new forms, or with status {@code 400 (Bad Request)} if the forms has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forms")
    public ResponseEntity<Forms> createForms(@RequestBody Forms forms) throws URISyntaxException {
        log.debug("REST request to save Forms : {}", forms);
        if (forms.getId() != null) {
            throw new BadRequestAlertException("A new forms cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Forms result = formsRepository.save(forms);
        return ResponseEntity
            .created(new URI("/api/forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forms/:id} : Updates an existing forms.
     *
     * @param id the id of the forms to save.
     * @param forms the forms to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forms,
     * or with status {@code 400 (Bad Request)} if the forms is not valid,
     * or with status {@code 500 (Internal Server Error)} if the forms couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forms/{id}")
    public ResponseEntity<Forms> updateForms(@PathVariable(value = "id", required = false) final Long id, @RequestBody Forms forms)
        throws URISyntaxException {
        log.debug("REST request to update Forms : {}, {}", id, forms);
        if (forms.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, forms.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Forms result = formsRepository.save(forms);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, forms.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /forms/:id} : Partial updates given fields of an existing forms, field will ignore if it is null
     *
     * @param id the id of the forms to save.
     * @param forms the forms to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forms,
     * or with status {@code 400 (Bad Request)} if the forms is not valid,
     * or with status {@code 404 (Not Found)} if the forms is not found,
     * or with status {@code 500 (Internal Server Error)} if the forms couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/forms/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Forms> partialUpdateForms(@PathVariable(value = "id", required = false) final Long id, @RequestBody Forms forms)
        throws URISyntaxException {
        log.debug("REST request to partial update Forms partially : {}, {}", id, forms);
        if (forms.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, forms.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Forms> result = formsRepository
            .findById(forms.getId())
            .map(
                existingForms -> {
                    if (forms.getName() != null) {
                        existingForms.setName(forms.getName());
                    }
                    if (forms.getCynapseId() != null) {
                        existingForms.setCynapseId(forms.getCynapseId());
                    }
                    if (forms.getEmailId() != null) {
                        existingForms.setEmailId(forms.getEmailId());
                    }
                    if (forms.getPhoneNumber() != null) {
                        existingForms.setPhoneNumber(forms.getPhoneNumber());
                    }
                    if (forms.getCreatedDate() != null) {
                        existingForms.setCreatedDate(forms.getCreatedDate());
                    }
                    if (forms.getReferalCode() != null) {
                        existingForms.setReferalCode(forms.getReferalCode());
                    }
                    if (forms.getFormDocument() != null) {
                        existingForms.setFormDocument(forms.getFormDocument());
                    }
                    if (forms.getFormDocumentContentType() != null) {
                        existingForms.setFormDocumentContentType(forms.getFormDocumentContentType());
                    }

                    return existingForms;
                }
            )
            .map(formsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, forms.getId().toString())
        );
    }

    /**
     * {@code GET  /forms} : get all the forms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of forms in body.
     */
    @GetMapping("/forms")
    public List<Forms> getAllForms() {
        log.debug("REST request to get all Forms");
        return formsRepository.findAll();
    }

    /**
     * {@code GET  /forms/:id} : get the "id" forms.
     *
     * @param id the id of the forms to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the forms, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forms/{id}")
    public ResponseEntity<Forms> getForms(@PathVariable Long id) {
        log.debug("REST request to get Forms : {}", id);
        Optional<Forms> forms = formsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(forms);
    }

    /**
     * {@code DELETE  /forms/:id} : delete the "id" forms.
     *
     * @param id the id of the forms to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forms/{id}")
    public ResponseEntity<Void> deleteForms(@PathVariable Long id) {
        log.debug("REST request to delete Forms : {}", id);
        formsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
