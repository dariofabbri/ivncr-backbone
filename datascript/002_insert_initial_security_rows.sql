INSERT INTO sec_user (username, password, firstname, lastname, description)
  VALUES ('admin', 'admin', 'Administrator', 'Administrator', null);

INSERT INTO sec_role (rolename, description)
  VALUES ('admin', null);

INSERT INTO sec_permission (permstring)
  VALUES ('*');

INSERT INTO sec_user_role (userid, roleid)
  VALUES (
	(select id from sec_user where username = 'admin'), 
	(select id from sec_role where rolename = 'admin')
  );

INSERT INTO sec_role_permission (roleid, permissionid)
  VALUES (
	(select id from sec_role where rolename = 'admin'), 
	(select id from sec_permission where permstring = '*')
  );
