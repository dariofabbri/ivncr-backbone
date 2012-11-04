ALTER TABLE sec_user DROP COLUMN password;

ALTER TABLE sec_user ADD digest varchar(255);
ALTER TABLE sec_user ADD salt varchar(255);
ALTER TABLE sec_user ADD iterations integer;

UPDATE sec_user SET 
  digest = 'bcb1e28282e7bab07fbe4191662f7c0bfca5426471f929b307e1353665466d0d0293437b9b2302678397bd0c5167049f79bf1facb0b652c73ab337f283c8f780',
  salt = '8521903395d92a798dfe4972867e8aead2861a458abc5fb488bde37825890d6b2ac1300b7cfce53020a1dbb1f3bbacc74b7410fab45b38b14d8990d2b673d8a5',
  iterations = 50000
  WHERE id = 1;
