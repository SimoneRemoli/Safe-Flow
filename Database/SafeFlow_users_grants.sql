-- ===============================
-- Safe Flow - Users & Permissions
-- ===============================

DROP USER IF EXISTS `login_User`@`%`;
DROP USER IF EXISTS `traveler`@`%`;
DROP USER IF EXISTS `admin_amministratore_safeflow`@`%`;

-- ===============================
-- CREATE USERS WITH PASSWORDS
-- ===============================

CREATE USER `login_User`@`%`
IDENTIFIED BY 'login_pass';

CREATE USER `traveler`@`%`
IDENTIFIED BY 'traveler';

CREATE USER `admin_amministratore_safeflow`@`%`
IDENTIFIED BY 'adminroute';

-- ===============================
-- LOGIN USER
-- ===============================
GRANT USAGE ON *.* TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`getAllCity` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`login_User` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`register` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`register_User` TO `login_User`@`%`;

-- ===============================
-- TRAVELER
-- ===============================
GRANT USAGE ON *.* TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`getAllCity` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`getMessages` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`spCommunication` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`MarkCommunicationAsRead` TO `traveler`@`%`;

-- ===============================
-- ADMIN
-- ===============================
GRANT USAGE ON *.* TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`getMessages` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`spCommunication` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`ListAdmins` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`CreateAdmin` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`DeleteAdminByCodiceFiscale` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`ListTravelers` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`DeleteTravelerByCodiceFiscale` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`ApproveTravelerCommunication` TO `admin_amministratore_safeflow`@`%`;
GRANT EXECUTE ON PROCEDURE `SafeFlow_Update`.`RejectTravelerCommunication` TO `admin_amministratore_safeflow`@`%`;

FLUSH PRIVILEGES;
