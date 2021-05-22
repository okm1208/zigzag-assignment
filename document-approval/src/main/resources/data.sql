
INSERT INTO account
	(
		account_no
		, account_id
		, password
	)
VALUES
	(
		1
		, 'admin'
		, '$2a$10$cBGaBw4qj6f5GH1KYDQ2HO.C4OY42O89XF/g7iU1Mau5lyac/vcCm'
	);
INSERT INTO account_authorities
	(
		account_no
		, authority
	)
VALUES
	(
		1
		, 'ROLE_USER'
	);

INSERT INTO account
	(
		account_no
		, account_id
		, password
	)
VALUES
	(
		2
		, 'admin2'
		, '$2a$10$cBGaBw4qj6f5GH1KYDQ2HO.C4OY42O89XF/g7iU1Mau5lyac/vcCm'
	);
INSERT INTO account_authorities
	(
		account_no
		, authority
	)
VALUES
	(
		2
		, 'ROLE_USER'
	);
INSERT INTO account
	(
		account_no
		, account_id
		, password
	)
VALUES
	(
		3
		, 'admin3'
		, '$2a$10$cBGaBw4qj6f5GH1KYDQ2HO.C4OY42O89XF/g7iU1Mau5lyac/vcCm'
	);
INSERT INTO account_authorities
	(
		account_no
		, authority
	)
VALUES
	(
		3
		, 'ROLE_USER'
	);