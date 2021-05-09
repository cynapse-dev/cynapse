package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Forms;
import com.mycompany.myapp.repository.FormsRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FormsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CYNAPSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_CYNAPSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REFERAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REFERAL_CODE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FORM_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FORM_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FORM_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FORM_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormsRepository formsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormsMockMvc;

    private Forms forms;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forms createEntity(EntityManager em) {
        Forms forms = new Forms()
            .name(DEFAULT_NAME)
            .cynapseId(DEFAULT_CYNAPSE_ID)
            .emailId(DEFAULT_EMAIL_ID)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .createdDate(DEFAULT_CREATED_DATE)
            .referalCode(DEFAULT_REFERAL_CODE)
            .formDocument(DEFAULT_FORM_DOCUMENT)
            .formDocumentContentType(DEFAULT_FORM_DOCUMENT_CONTENT_TYPE);
        return forms;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forms createUpdatedEntity(EntityManager em) {
        Forms forms = new Forms()
            .name(UPDATED_NAME)
            .cynapseId(UPDATED_CYNAPSE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdDate(UPDATED_CREATED_DATE)
            .referalCode(UPDATED_REFERAL_CODE)
            .formDocument(UPDATED_FORM_DOCUMENT)
            .formDocumentContentType(UPDATED_FORM_DOCUMENT_CONTENT_TYPE);
        return forms;
    }

    @BeforeEach
    public void initTest() {
        forms = createEntity(em);
    }

    @Test
    @Transactional
    void createForms() throws Exception {
        int databaseSizeBeforeCreate = formsRepository.findAll().size();
        // Create the Forms
        restFormsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forms)))
            .andExpect(status().isCreated());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeCreate + 1);
        Forms testForms = formsList.get(formsList.size() - 1);
        assertThat(testForms.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testForms.getCynapseId()).isEqualTo(DEFAULT_CYNAPSE_ID);
        assertThat(testForms.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testForms.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testForms.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testForms.getReferalCode()).isEqualTo(DEFAULT_REFERAL_CODE);
        assertThat(testForms.getFormDocument()).isEqualTo(DEFAULT_FORM_DOCUMENT);
        assertThat(testForms.getFormDocumentContentType()).isEqualTo(DEFAULT_FORM_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createFormsWithExistingId() throws Exception {
        // Create the Forms with an existing ID
        forms.setId(1L);

        int databaseSizeBeforeCreate = formsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forms)))
            .andExpect(status().isBadRequest());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllForms() throws Exception {
        // Initialize the database
        formsRepository.saveAndFlush(forms);

        // Get all the formsList
        restFormsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forms.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cynapseId").value(hasItem(DEFAULT_CYNAPSE_ID)))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].referalCode").value(hasItem(DEFAULT_REFERAL_CODE)))
            .andExpect(jsonPath("$.[*].formDocumentContentType").value(hasItem(DEFAULT_FORM_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].formDocument").value(hasItem(Base64Utils.encodeToString(DEFAULT_FORM_DOCUMENT))));
    }

    @Test
    @Transactional
    void getForms() throws Exception {
        // Initialize the database
        formsRepository.saveAndFlush(forms);

        // Get the forms
        restFormsMockMvc
            .perform(get(ENTITY_API_URL_ID, forms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(forms.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cynapseId").value(DEFAULT_CYNAPSE_ID))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.referalCode").value(DEFAULT_REFERAL_CODE))
            .andExpect(jsonPath("$.formDocumentContentType").value(DEFAULT_FORM_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.formDocument").value(Base64Utils.encodeToString(DEFAULT_FORM_DOCUMENT)));
    }

    @Test
    @Transactional
    void getNonExistingForms() throws Exception {
        // Get the forms
        restFormsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewForms() throws Exception {
        // Initialize the database
        formsRepository.saveAndFlush(forms);

        int databaseSizeBeforeUpdate = formsRepository.findAll().size();

        // Update the forms
        Forms updatedForms = formsRepository.findById(forms.getId()).get();
        // Disconnect from session so that the updates on updatedForms are not directly saved in db
        em.detach(updatedForms);
        updatedForms
            .name(UPDATED_NAME)
            .cynapseId(UPDATED_CYNAPSE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdDate(UPDATED_CREATED_DATE)
            .referalCode(UPDATED_REFERAL_CODE)
            .formDocument(UPDATED_FORM_DOCUMENT)
            .formDocumentContentType(UPDATED_FORM_DOCUMENT_CONTENT_TYPE);

        restFormsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedForms.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedForms))
            )
            .andExpect(status().isOk());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
        Forms testForms = formsList.get(formsList.size() - 1);
        assertThat(testForms.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForms.getCynapseId()).isEqualTo(UPDATED_CYNAPSE_ID);
        assertThat(testForms.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testForms.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testForms.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testForms.getReferalCode()).isEqualTo(UPDATED_REFERAL_CODE);
        assertThat(testForms.getFormDocument()).isEqualTo(UPDATED_FORM_DOCUMENT);
        assertThat(testForms.getFormDocumentContentType()).isEqualTo(UPDATED_FORM_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingForms() throws Exception {
        int databaseSizeBeforeUpdate = formsRepository.findAll().size();
        forms.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, forms.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForms() throws Exception {
        int databaseSizeBeforeUpdate = formsRepository.findAll().size();
        forms.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForms() throws Exception {
        int databaseSizeBeforeUpdate = formsRepository.findAll().size();
        forms.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forms)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormsWithPatch() throws Exception {
        // Initialize the database
        formsRepository.saveAndFlush(forms);

        int databaseSizeBeforeUpdate = formsRepository.findAll().size();

        // Update the forms using partial update
        Forms partialUpdatedForms = new Forms();
        partialUpdatedForms.setId(forms.getId());

        partialUpdatedForms
            .name(UPDATED_NAME)
            .cynapseId(UPDATED_CYNAPSE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdDate(UPDATED_CREATED_DATE);

        restFormsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForms.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForms))
            )
            .andExpect(status().isOk());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
        Forms testForms = formsList.get(formsList.size() - 1);
        assertThat(testForms.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForms.getCynapseId()).isEqualTo(UPDATED_CYNAPSE_ID);
        assertThat(testForms.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testForms.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testForms.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testForms.getReferalCode()).isEqualTo(DEFAULT_REFERAL_CODE);
        assertThat(testForms.getFormDocument()).isEqualTo(DEFAULT_FORM_DOCUMENT);
        assertThat(testForms.getFormDocumentContentType()).isEqualTo(DEFAULT_FORM_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFormsWithPatch() throws Exception {
        // Initialize the database
        formsRepository.saveAndFlush(forms);

        int databaseSizeBeforeUpdate = formsRepository.findAll().size();

        // Update the forms using partial update
        Forms partialUpdatedForms = new Forms();
        partialUpdatedForms.setId(forms.getId());

        partialUpdatedForms
            .name(UPDATED_NAME)
            .cynapseId(UPDATED_CYNAPSE_ID)
            .emailId(UPDATED_EMAIL_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdDate(UPDATED_CREATED_DATE)
            .referalCode(UPDATED_REFERAL_CODE)
            .formDocument(UPDATED_FORM_DOCUMENT)
            .formDocumentContentType(UPDATED_FORM_DOCUMENT_CONTENT_TYPE);

        restFormsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForms.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForms))
            )
            .andExpect(status().isOk());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
        Forms testForms = formsList.get(formsList.size() - 1);
        assertThat(testForms.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForms.getCynapseId()).isEqualTo(UPDATED_CYNAPSE_ID);
        assertThat(testForms.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testForms.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testForms.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testForms.getReferalCode()).isEqualTo(UPDATED_REFERAL_CODE);
        assertThat(testForms.getFormDocument()).isEqualTo(UPDATED_FORM_DOCUMENT);
        assertThat(testForms.getFormDocumentContentType()).isEqualTo(UPDATED_FORM_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingForms() throws Exception {
        int databaseSizeBeforeUpdate = formsRepository.findAll().size();
        forms.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, forms.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForms() throws Exception {
        int databaseSizeBeforeUpdate = formsRepository.findAll().size();
        forms.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forms))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForms() throws Exception {
        int databaseSizeBeforeUpdate = formsRepository.findAll().size();
        forms.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(forms)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forms in the database
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForms() throws Exception {
        // Initialize the database
        formsRepository.saveAndFlush(forms);

        int databaseSizeBeforeDelete = formsRepository.findAll().size();

        // Delete the forms
        restFormsMockMvc
            .perform(delete(ENTITY_API_URL_ID, forms.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Forms> formsList = formsRepository.findAll();
        assertThat(formsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
