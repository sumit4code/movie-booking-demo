db.createUser(
  {
    user: 'intuit',
    pwd: 'intuit123',
    roles: [ { role: 'readWrite', db: 'movie' } ],
    mechanisms : ["SCRAM-SHA-1"]
  }
);
