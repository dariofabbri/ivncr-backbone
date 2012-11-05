package it.dariofabbri.ivncr.service.local.user;

import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.Query;

import org.hibernate.Session;

public class QueryUserByUsernameFirstNameLastNameDescription extends Query<User> {

	private String username;
	private String firstName;
	private String lastName;
	private String description;
	
	public QueryUserByUsernameFirstNameLastNameDescription(Session session) {
		
		super(session);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	protected boolean checkQueryArguments() {

		return true;
	}

	@Override
	protected String getCountHql() {

		String hql = 
				"select count(*) " +
				"from User use " +
				"where 1 = 1 ";
		
		if(username != null)
			hql += "and upper(use.username) like :username ";
		
		if(firstName != null)
			hql += "and upper(use.firstName) like :firstName ";
		
		if(lastName != null)
			hql += "and upper(use.lastName) like :lastName ";
		
		if(description != null)
			hql += "and upper(use.description) like :description ";

		return hql;
	}

	@Override
	protected String getQueryHql() {

		String hql = 
				"from User use " +
				"where 1 = 1 ";
		
		if(username != null)
			hql += "and upper(use.username) like :username ";
		
		if(firstName != null)
			hql += "and upper(use.firstName) like :firstName ";
		
		if(lastName != null)
			hql += "and upper(use.lastName) like :lastName ";
		
		if(description != null)
			hql += "and upper(use.description) like :description ";

		hql += "order by use.id ";
		
		return hql;
	}

	@Override
	protected void setQueryArguments(org.hibernate.Query q) {

		String[] named_params = q.getNamedParameters();
		for (int i = 0; i < named_params.length; ++i) {
			String param = named_params[i];

			if (param.equals("username"))
				q.setParameter("username", "%" + username.toUpperCase() + "%");

			else if (param.equals("firstName"))
				q.setParameter("firstName", "%" + firstName.toUpperCase() + "%");
			
			else if (param.equals("lastName"))
				q.setParameter("lastName", "%" + lastName.toUpperCase() + "%");
			
			else if (param.equals("description"))
				q.setParameter("description",  "%" + description.toUpperCase() + "%");
		}
	}
}