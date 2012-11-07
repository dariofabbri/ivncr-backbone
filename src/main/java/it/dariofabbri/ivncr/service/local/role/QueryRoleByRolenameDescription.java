package it.dariofabbri.ivncr.service.local.role;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.Query;

import org.hibernate.Session;

public class QueryRoleByRolenameDescription extends Query<Role> {

	private String rolename;
	private String description;
	
	public QueryRoleByRolenameDescription(Session session) {
		
		super(session);
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
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
				"from Role rol " +
				"where 1 = 1 ";
		
		if(rolename != null)
			hql += "and upper(rol.rolename) like :rolename ";
		
		if(description != null)
			hql += "and upper(rol.description) like :description ";

		return hql;
	}

	@Override
	protected String getQueryHql() {

		String hql = 
				"from Role rol " +
				"where 1 = 1 ";
		
		if(rolename != null)
			hql += "and upper(rol.rolename) like :rolename ";
		
		if(description != null)
			hql += "and upper(rol.description) like :description ";

		hql += "order by rol.id ";
		
		return hql;
	}

	@Override
	protected void setQueryArguments(org.hibernate.Query q) {

		String[] named_params = q.getNamedParameters();
		for (int i = 0; i < named_params.length; ++i) {
			String param = named_params[i];

			if (param.equals("rolename"))
				q.setParameter("rolename", "%" + rolename.toUpperCase() + "%");
			
			else if (param.equals("description"))
				q.setParameter("description",  "%" + description.toUpperCase() + "%");
		}
	}
}