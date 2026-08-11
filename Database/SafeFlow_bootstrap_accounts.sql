-- ===============================
-- Safe Flow - Bootstrap Accounts
-- ===============================
-- Optional script for a clean but usable installation.
-- Load it after SafeFlow_Update.sql when an initial admin login is needed.

USE SafeFlow_Update;

DELETE FROM `Permessi`
WHERE `Email` = 'admin@safeflow.local'
   OR `Codice_Fiscale` = 'SFLWADMIN0000000';

INSERT INTO `Permessi` (
    `Nome`,
    `Cognome`,
    `Email`,
    `Password`,
    `Ruolo`,
    `Codice_Fiscale`
) VALUES (
    'SafeFlow',
    'Administrator',
    'admin@safeflow.local',
    'admin123!',
    '2',
    'SFLWADMIN0000000'
);
