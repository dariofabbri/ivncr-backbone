package it.dariofabbri.ivncr.service.local.contact;

import it.dariofabbri.ivncr.model.contact.Contact;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.Service;

public interface ContactService extends Service {

	QueryResult<Contact> listContacts(
			String firstName,
			String lastName,
			String phoneNumber,
			Integer offset,
			Integer limit);

	Contact retrieveContactById(Integer id);

	boolean deleteContactById(Integer id);

	boolean createContact(String firstName, String lastName, String phoneNumber);

	boolean updateContact(Integer id, String firstName, String lastName, String phoneNumber);
}