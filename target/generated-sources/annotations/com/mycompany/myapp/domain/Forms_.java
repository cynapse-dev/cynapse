package com.mycompany.myapp.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Forms.class)
public abstract class Forms_ {

	public static volatile SingularAttribute<Forms, String> referalCode;
	public static volatile SingularAttribute<Forms, String> phoneNumber;
	public static volatile SingularAttribute<Forms, LocalDate> createdDate;
	public static volatile SingularAttribute<Forms, String> formDocumentContentType;
	public static volatile SingularAttribute<Forms, String> name;
	public static volatile SingularAttribute<Forms, String> emailId;
	public static volatile SingularAttribute<Forms, Long> id;
	public static volatile SingularAttribute<Forms, String> cynapseId;
	public static volatile SingularAttribute<Forms, User> user;
	public static volatile SingularAttribute<Forms, byte[]> formDocument;

	public static final String REFERAL_CODE = "referalCode";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String CREATED_DATE = "createdDate";
	public static final String FORM_DOCUMENT_CONTENT_TYPE = "formDocumentContentType";
	public static final String NAME = "name";
	public static final String EMAIL_ID = "emailId";
	public static final String ID = "id";
	public static final String CYNAPSE_ID = "cynapseId";
	public static final String USER = "user";
	public static final String FORM_DOCUMENT = "formDocument";

}

