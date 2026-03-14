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
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getallcity` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getfermatabyid` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`login_user` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`restituiscistazioni` TO `login_User`@`%`;

-- ===============================
-- TRAVELER
-- ===============================
GRANT USAGE ON *.* TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getAllCity` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`RestituisciStazioni` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`GetFermataById` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getpath` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`savepayment` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`saveroute` TO `traveler`@`%`;
GRANT EXECUTE ON `RouteX_Update`.* TO `traveler`@`%`;

-- ===============================
-- ADMIN
-- ===============================
GRANT USAGE ON *.* TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getallpathinfo` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getmessages` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`spcommunication` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`spsolvedmessage` TO `admin_amministratore_routex`@`%`;

-- ===============================
-- WORKER
-- ===============================
GRANT USAGE ON *.* TO `worker`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getmessages` TO `worker`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`spsolvedmessage` TO `worker`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`viewworkschedule` TO `worker`@`%`;

FLUSH PRIVILEGES;
