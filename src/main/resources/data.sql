INSERT INTO
  oauth_client_details (
    client_id,
    client_secret,
    resource_ids,
    scope,
    authorized_grant_types,
    authorities,
    access_token_validity,
    refresh_token_validity
  )
VALUES
  (
    'client',
    '$2a$10$4R/rWflN2RDiGZ3TvGplN.Z7fpILYAop9kJKqk7FgZnHCGhwFSGYS',--<= 'secret'
    'oauth2-resource',
    'read,write',
    'authorization_code,check_token,refresh_token,password',
    'ROLE_CLIENT',
    30,
    21600
  );
