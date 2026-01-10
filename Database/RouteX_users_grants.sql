-- ===============================
-- RouteX - Users & Permissions
-- ===============================

DROP USER IF EXISTS `login_User`@`%`;
DROP USER IF EXISTS `traveler`@`%`;
DROP USER IF EXISTS `admin_amministratore_routex`@`%`;
DROP USER IF EXISTS `worker`@`%`;

-- ===============================
-- CREATE USERS WITH PASSWORDS
-- ===============================

CREATE USER `login_User`@`%`
IDENTIFIED BY 'login_pass';

CREATE USER `traveler`@`%`
IDENTIFIED BY 'traveler';

CREATE USER `admin_amministratore_routex`@`%`
IDENTIFIED BY 'adminroute';

CREATE USER `worker`@`%`
IDENTIFIED BY 'worker';

-- ===============================
-- LOGIN USER
-- ===============================
GRANT USAGE ON *.* TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`getallcity` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`getfermatabyid` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`login_user` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`restituiscistazioni` TO `login_User`@`%`;

-- ===============================
-- TRAVELER
-- ===============================
GRANT USAGE ON *.* TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`getpath` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`savepayment` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`saveroute` TO `traveler`@`%`;

-- ===============================
-- ADMIN
-- ===============================
GRANT USAGE ON *.* TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`getallpathinfo` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`spcommunication` TO `admin_amministratore_routex`@`%`;

-- ===============================
-- WORKER
-- ===============================
GRANT USAGE ON *.* TO `worker`@`%`;
GRANT EXECUTE ON PROCEDURE `routex_update`.`viewworkschedule` TO `worker`@`%`;