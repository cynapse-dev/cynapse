package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Forms.
 */
@Entity
@Table(name = "forms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Forms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cynapse_id")
    private String cynapseId;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "referal_code")
    private String referalCode;

    @Lob
    @Column(name = "form_document")
    private byte[] formDocument;

    @Column(name = "form_document_content_type")
    private String formDocumentContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Forms id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Forms name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCynapseId() {
        return this.cynapseId;
    }

    public Forms cynapseId(String cynapseId) {
        this.cynapseId = cynapseId;
        return this;
    }

    public void setCynapseId(String cynapseId) {
        this.cynapseId = cynapseId;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public Forms emailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Forms phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Forms createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getReferalCode() {
        return this.referalCode;
    }

    public Forms referalCode(String referalCode) {
        this.referalCode = referalCode;
        return this;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public byte[] getFormDocument() {
        return this.formDocument;
    }

    public Forms formDocument(byte[] formDocument) {
        this.formDocument = formDocument;
        return this;
    }

    public void setFormDocument(byte[] formDocument) {
        this.formDocument = formDocument;
    }

    public String getFormDocumentContentType() {
        return this.formDocumentContentType;
    }

    public Forms formDocumentContentType(String formDocumentContentType) {
        this.formDocumentContentType = formDocumentContentType;
        return this;
    }

    public void setFormDocumentContentType(String formDocumentContentType) {
        this.formDocumentContentType = formDocumentContentType;
    }

    public User getUser() {
        return this.user;
    }

    public Forms user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forms)) {
            return false;
        }
        return id != null && id.equals(((Forms) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Forms{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cynapseId='" + getCynapseId() + "'" +
            ", emailId='" + getEmailId() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", referalCode='" + getReferalCode() + "'" +
            ", formDocument='" + getFormDocument() + "'" +
            ", formDocumentContentType='" + getFormDocumentContentType() + "'" +
            "}";
    }
}
