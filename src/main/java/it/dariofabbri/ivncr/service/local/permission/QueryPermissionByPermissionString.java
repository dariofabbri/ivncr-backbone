package it.dariofabbri.ivncr.service.local.permission;

import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.service.local.Query;

import org.hibernate.Session;

public class QueryPermissionByPermissionString extends Query<Permission> {

	private String permissionString;
	
	public QueryPermissionByPermissionString(Session session) {
		
		super(session);
	}

	public String getPermissionString() {
		return permissionString;
	}

	public void setPermissionString(String permissionString) {
		this.permissionString = permissionString;
	}

	@Override
	protected boolean checkQueryArguments() {

		return true;
	}

	@Override
	protected String getCountHql() {

		String hql = 
				"select count(*) " +
				"from Permission per " +
				"where 1 = 1 ";
		
		if(permissionString != null)
			hql += "and upper(per.permissionString) like :permissionString ";

		return hql;
	}

	@Override
	protected String getQueryHql() {

		String hql = 
				"from Permission per " +
				"where 1 = 1 ";

		if(permissionString != null)
			hql += "and upper(per.permissionString) like :permissionString ";
		
		hql += "order by per.permissionString ";
		
		return hql;
	}

	@Override
	protected void setQueryArguments(org.hibernate.Query q) {

		String[] named_params = q.getNamedParameters();
		for (int i = 0; i < named_params.length; ++i) {
			String param = named_params[i];

			if (param.equals("permissionString"))
				q.setParameter("permissionString", "%" + permissionString.toUpperCase() + "%");
		}
	}
}