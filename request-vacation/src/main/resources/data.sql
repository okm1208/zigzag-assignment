
INSERT INTO account
	(
		account_no
		, account_id
		, password
		, active
		, status
	)
VALUES
	(
		1
		, 'admin'
		, '$2a$10$cBGaBw4qj6f5GH1KYDQ2HO.C4OY42O89XF/g7iU1Mau5lyac/vcCm'
		, 'true'
		, 'ACTIVE'
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
INSERT INTO account_vacation_info
    (
        account_account_no,
        occurs_days,
        use_days,
        remaining_days
    )
VALUES
    (
        1,
        15.00,
        0.00,
        15.00
    );